import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления данными авиакомпании
 * Обрабатывает операции с рейсами, бронированиями и пассажирами
 */
public class AirlineService {
    private List<Flight> flights;
    private List<Booking> bookings;
    private List<Passenger> passengers;
    private DBConnection dbConnection;

    public AirlineService() {
        this.flights = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.passengers = new ArrayList<>();
        this.dbConnection = new DBConnection();

        // Инициализация примером данными
        initializeSampleData();
    }

    /**
     * Инициализация примера данных для тестирования
     */
    private void initializeSampleData() {
        // Добавляем несколько примеров рейсов
        flights.add(new Flight("FL001", 250.0));
        flights.add(new Flight("FL002", 150.0));
        flights.add(new Flight("FL003", 300.0));
        flights.add(new Flight("FL004", 200.0));

        // Добавляем примеры пассажиров
        passengers.add(new Passenger("Ivan Petrov", 35));
        passengers.add(new Passenger("Maria Sidorova", 28));
        passengers.add(new Passenger("Alexei Ivanov", 42));
    }

    // ============ FLIGHTS OPERATIONS ============

    /**
     * Получить все рейсы
     */
    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    /**
     * Добавить новый рейс
     */
    public void addFlight(Flight flight) {
        if (flight != null && !flights.contains(flight)) {
            flights.add(flight);
            // Сохраняем в БД
            dbConnection.saveFlight(flight);
        }
    }

    /**
     * Получить рейс по номеру
     */
    public Flight getFlightByNumber(String flightNumber) {
        return flights.stream()
                .filter(f -> f.getNumber().equals(flightNumber))
                .findFirst()
                .orElse(null);
    }

    /**
     * Обновить рейс
     */
    public void updateFlight(Flight flight) {
        if (flight == null) return;

        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getNumber().equals(flight.getNumber())) {
                flights.set(i, flight);
                dbConnection.updateFlight(flight);
                break;
            }
        }
    }

    /**
     * Удалить рейс
     */
    public void deleteFlight(String flightNumber) {
        flights.removeIf(f -> f.getNumber().equals(flightNumber));
        dbConnection.deleteFlight(flightNumber);
    }

    /**
     * Поиск рейсов по цене (диапазон)
     */
    public List<Flight> searchFlightsByDestination(String destination) {
        // Так как в твоем Flight нет destination, возвращаем все рейсы
        // Эту функцию можно доработать позже
        return new ArrayList<>(flights);
    }

    // ============ BOOKINGS OPERATIONS ============

    /**
     * Получить все бронирования
     */
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    /**
     * Добавить новое бронирование
     */
    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking);
            // Сохраняем в БД
            dbConnection.saveBooking(booking);
        }
    }

    /**
     * Получить бронирования по имени пассажира
     */
    public List<Booking> getBookingsByPassengerName(String passengerName) {
        return bookings.stream()
                .filter(b -> b.passenger.getName().equalsIgnoreCase(passengerName))
                .collect(Collectors.toList());
    }

    /**
     * Получить бронирования по номеру рейса
     */
    public List<Booking> getBookingsByFlightNumber(String flightNumber) {
        return bookings.stream()
                .filter(b -> b.flight.getNumber().equals(flightNumber))
                .collect(Collectors.toList());
    }

    /**
     * Обновить бронирование
     */
    public void updateBooking(Booking booking) {
        if (booking == null) return;

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).passenger.getName().equals(booking.passenger.getName()) &&
                    bookings.get(i).flight.getNumber().equals(booking.flight.getNumber())) {
                bookings.set(i, booking);
                dbConnection.updateBooking(booking);
                break;
            }
        }
    }

    /**
     * Удалить бронирование
     */
    public void deleteBooking(String passengerName, String flightNumber) {
        bookings.removeIf(b ->
                b.passenger.getName().equalsIgnoreCase(passengerName) &&
                        b.flight.getNumber().equals(flightNumber)
        );
        dbConnection.deleteBooking(passengerName + "-" + flightNumber);
    }

    // ============ PASSENGERS OPERATIONS ============

    /**
     * Получить всех пассажиров
     */
    public List<Passenger> getAllPassengers() {
        return new ArrayList<>(passengers);
    }

    /**
     * Добавить нового пассажира
     */
    public void addPassenger(Passenger passenger) {
        if (passenger != null && !passengers.contains(passenger)) {
            passengers.add(passenger);
            // Сохраняем в БД
            dbConnection.savePassenger(passenger);
        }
    }

    /**
     * Получить пассажира по имени
     */
    public Passenger getPassengerByName(String name) {
        return passengers.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Поиск пассажиров по имени (частичное совпадение)
     */
    public List<Passenger> searchPassengersByName(String name) {
        return passengers.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Получить пассажиров по возрасту
     */
    public List<Passenger> getPassengersByAge(int age) {
        return passengers.stream()
                .filter(p -> p.getAge() == age)
                .collect(Collectors.toList());
    }

    /**
     * Получить пассажиров старше определенного возраста
     */
    public List<Passenger> getPassengersOlderThan(int age) {
        return passengers.stream()
                .filter(p -> p.getAge() > age)
                .collect(Collectors.toList());
    }

    /**
     * Обновить пассажира
     */
    public void updatePassenger(Passenger passenger) {
        if (passenger == null) return;

        for (int i = 0; i < passengers.size(); i++) {
            if (passengers.get(i).getName().equals(passenger.getName())) {
                passengers.set(i, passenger);
                dbConnection.updatePassenger(passenger);
                break;
            }
        }
    }

    /**
     * Удалить пассажира
     */
    public void deletePassenger(String passengerName) {
        passengers.removeIf(p -> p.getName().equalsIgnoreCase(passengerName));
        dbConnection.deletePassenger(passengerName);
    }

    // ============ UTILITY METHODS ============

    /**
     * Получить статистику
     */
    public String getStatistics() {
        return String.format(
                "📊 Статистика системы:\n" +
                        "✈️  Рейсы: %d\n" +
                        "📝 Бронирования: %d\n" +
                        "👥 Пассажиры: %d",
                flights.size(),
                bookings.size(),
                passengers.size()
        );
    }

    /**
     * Создать бронирование
     */
    public Booking createBooking(String passengerName, String flightNumber) {
        Passenger passenger = getPassengerByName(passengerName);
        Flight flight = getFlightByNumber(flightNumber);

        if (passenger != null && flight != null) {
            Booking booking = new Booking(flight, passenger);
            addBooking(booking);
            return booking;
        }
        return null;
    }
}