import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * REST API сервер для системы бронирования авиабилетов
 * Обрабатывает HTTP запросы и отправляет JSON ответы
 */
public class RestApiServer {
    private final HttpServer server;
    private final int port;
    private final AirlineService airlineService;
    private final Gson gson;

    public RestApiServer(int port) throws IOException {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.airlineService = new AirlineService();
        this.gson = new Gson();

        // Регистрация эндпоинтов
        setupEndpoints();
    }

    /**
     * Настройка всех API эндпоинтов
     */
    private void setupEndpoints() {
        // Flights endpoints
        server.createContext("/api/flights", new FlightsHandler());

        // Bookings endpoints
        server.createContext("/api/bookings", new BookingsHandler());

        // Passengers endpoints
        server.createContext("/api/passengers", new PassengersHandler());

        // Health check
        server.createContext("/api/health", new HealthHandler());

        // Welcome endpoint
        server.createContext("/", new WelcomeHandler());
    }

    /**
     * Запуск сервера
     */
    public void start() {
        server.setExecutor(null);
        server.start();
        System.out.println("✅ REST API сервер запущен на http://localhost:" + port);
    }

    /**
     * Остановка сервера
     */
    @SuppressWarnings("unused")
    public void stop() {
        server.stop(0);
        System.out.println("❌ REST API сервер остановлен");
    }

    /**
     * Читать тело запроса в строку (Java 8 совместимо)
     */
    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = is.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }

        return sb.toString();
    }

    /**
     * Отправка JSON ответа
     */
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    /**
     * Отправка ошибки в JSON формате
     */
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("error", errorMessage);
        errorJson.addProperty("status", statusCode);

        String jsonResponse = gson.toJson(errorJson);
        sendJsonResponse(exchange, statusCode, jsonResponse);
    }

    /**
     * Обработчик для рейсов (GET, POST)
     */
    private class FlightsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            try {
                if ("GET".equals(method)) {
                    // GET /api/flights - получить все рейсы
                    String response = gson.toJson(airlineService.getAllFlights());
                    sendJsonResponse(exchange, 200, response);

                } else if ("POST".equals(method)) {
                    // POST /api/flights - добавить новый рейс
                    String requestBody = readRequestBody(exchange);
                    try {
                        Flight flight = gson.fromJson(requestBody, Flight.class);
                        airlineService.addFlight(flight);
                        JsonObject response = new JsonObject();
                        response.addProperty("message", "Рейс успешно добавлен");
                        response.addProperty("flightNumber", flight.getNumber());
                        sendJsonResponse(exchange, 201, gson.toJson(response));
                    } catch (Exception e) {
                        sendErrorResponse(exchange, 400, "Ошибка при добавлении рейса: " + e.getMessage());
                    }

                } else {
                    sendErrorResponse(exchange, 405, "Метод не поддерживается");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Внутренняя ошибка сервера: " + e.getMessage());
            }
        }
    }

    /**
     * Обработчик для бронирований (GET, POST)
     */
    private class BookingsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            try {
                if ("GET".equals(method)) {
                    // GET /api/bookings - получить все бронирования
                    String response = gson.toJson(airlineService.getAllBookings());
                    sendJsonResponse(exchange, 200, response);

                } else if ("POST".equals(method)) {
                    // POST /api/bookings - создать новое бронирование
                    String requestBody = readRequestBody(exchange);
                    try {
                        // Парсим JSON: { "passengerName": "...", "flightNumber": "..." }
                        JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
                        String passengerName = jsonObject.get("passengerName").getAsString();
                        String flightNumber = jsonObject.get("flightNumber").getAsString();

                        Booking booking = airlineService.createBooking(passengerName, flightNumber);

                        if (booking != null) {
                            JsonObject response = new JsonObject();
                            response.addProperty("message", "Бронирование успешно создано");
                            response.addProperty("passenger", booking.passenger.getName());
                            response.addProperty("flight", booking.flight.getNumber());
                            response.addProperty("price", booking.flight.getPrice());
                            sendJsonResponse(exchange, 201, gson.toJson(response));
                        } else {
                            sendErrorResponse(exchange, 400, "Пассажир или рейс не найдены");
                        }
                    } catch (Exception e) {
                        sendErrorResponse(exchange, 400, "Ошибка при создании бронирования: " + e.getMessage());
                    }

                } else {
                    sendErrorResponse(exchange, 405, "Метод не поддерживается");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Внутренняя ошибка сервера: " + e.getMessage());
            }
        }
    }

    /**
     * Обработчик для пассажиров (GET, POST)
     */
    private class PassengersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            try {
                if ("GET".equals(method)) {
                    // GET /api/passengers - получить всех пассажиров
                    String response = gson.toJson(airlineService.getAllPassengers());
                    sendJsonResponse(exchange, 200, response);

                } else if ("POST".equals(method)) {
                    // POST /api/passengers - добавить нового пассажира
                    String requestBody = readRequestBody(exchange);
                    try {
                        Passenger passenger = gson.fromJson(requestBody, Passenger.class);
                        airlineService.addPassenger(passenger);
                        JsonObject response = new JsonObject();
                        response.addProperty("message", "Пассажир успешно добавлен");
                        response.addProperty("passengerName", passenger.getName());
                        response.addProperty("age", passenger.getAge());
                        sendJsonResponse(exchange, 201, gson.toJson(response));
                    } catch (Exception e) {
                        sendErrorResponse(exchange, 400, "Ошибка при добавлении пассажира: " + e.getMessage());
                    }

                } else {
                    sendErrorResponse(exchange, 405, "Метод не поддерживается");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Внутренняя ошибка сервера: " + e.getMessage());
            }
        }
    }

    /**
     * Обработчик для проверки здоровья сервера
     */
    private class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                JsonObject health = new JsonObject();
                health.addProperty("status", "UP");
                health.addProperty("message", "REST API сервер работает нормально");
                health.addProperty("port", port);

                String response = gson.toJson(health);
                sendJsonResponse(exchange, 200, response);
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Ошибка при проверке здоровья: " + e.getMessage());
            }
        }
    }

    /**
     * Приветственный обработчик
     */
    private class WelcomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                JsonObject welcome = new JsonObject();
                welcome.addProperty("title", "Airline Reservation REST API");
                welcome.addProperty("version", "1.0.0");
                welcome.addProperty("status", "Running");

                String response = gson.toJson(welcome);
                sendJsonResponse(exchange, 200, response);
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Ошибка при обработке запроса: " + e.getMessage());
            }
        }
    }
}