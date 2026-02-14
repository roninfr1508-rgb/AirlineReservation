package com.airline.portal.service;

import com.airline.portal.config.DBConnection;
import com.airline.portal.domain.Flight;
import com.airline.portal.domain.Passenger;
import com.airline.portal.exception.InvalidData;
import com.airline.portal.exception.ResourceNotFound;
import com.airline.portal.domain.Booking;

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
    //Sorting
    public List<Flight> sortFlightsByPrice() {
        return flights.stream()
                .sorted((f1, f2) -> Double.compare(f1.getPrice(), f2.getPrice()))
                .collect(Collectors.toList());
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
     * Добавить новый рейс 4 ПУНКТ
     */
    public void addFlight(Flight flight) throws InvalidData {
        if (flight == null) {
            throw new InvalidData("Рейс не может быть null");
        }

        if (flight.getNumber() == null || flight.getNumber().trim().isEmpty()) {
            throw new InvalidData("Номер рейса не может быть пустым");
        }

        if (flight.getPrice() <= 0) {
            throw new InvalidData("Цена рейса должна быть больше 0");
        }

        if (!flights.contains(flight)) {
            flights.add(flight);
            dbConnection.saveFlight(flight);
        }
    }

    /**
     * Получить рейс по номеру 4 ПУНКТ
     */
    public Flight getFlightByNumber(String flightNumber) throws ResourceNotFound, InvalidData {
        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            throw new InvalidData("Номер рейса не может быть пустым");
        }

        return flights.stream()
                .filter(f -> f.getNumber().equals(flightNumber))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Рейс с номером " + flightNumber + " не найден"));


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


    /**
     * Получить бронирования по номеру рейса
     */
    public List<Booking> getBookingsByFlightNumber(String flightNumber) {
        return bookings.stream()
                .filter(b -> b.getFlight().getNumber().equals(flightNumber))
                .collect(Collectors.toList());
    }

    /**
     * Обновить бронирование
     */
    public void updateBooking(Booking booking) {
        if (booking == null) return;

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getPassenger().getName().equals(booking.getPassenger().getName()) &&
                    bookings.get(i).getFlight().getNumber().equals(booking.getFlight().getNumber())) {
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
                b.getPassenger().getName().equalsIgnoreCase(passengerName) &&
                        b.getFlight().getNumber().equals(flightNumber)
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
     * Добавить нового пассажира  4 ПУНКТ
     */
    public void addPassenger(Passenger passenger) throws InvalidData {
        if (passenger == null) {
            throw new InvalidData("Пассажир не может быть null");
        }

        if (passenger.getName() == null || passenger.getName().trim().isEmpty()) {
            throw new InvalidData("Имя пассажира не может быть пустым");
        }

        if (passenger.getAge() < 0 || passenger.getAge() > 100) {
            throw new InvalidData("Возраст должен быть от 0 до 100");
        }

        if (!passengers.contains(passenger)) {
            passengers.add(passenger);
            dbConnection.savePassenger(passenger);
        }
    }

    /**
     * Получить пассажира по имени 4 ПУНКТ
     */
    public Passenger getPassengerByName(String name) throws ResourceNotFound, InvalidData {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidData("Имя пассажира не может быть пустым");
        }

        return passengers.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Пассажир с именем " + name + " не найден"));
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
                "Статистика системы:\n" +
                        "Рейсы: %d\n" +
                        "Бронирования: %d\n" +
                        "Пассажиры: %d",
                flights.size(),
                bookings.size(),
                passengers.size()
        );
    }

    /**
     * Создать бронирование  4 ПУНКТ
     */
    public Booking createBooking(String passengerName, String flightNumber) throws InvalidData, ResourceNotFound {
        if (passengerName == null || passengerName.trim().isEmpty()) {
            throw new InvalidData("Имя пассажира не может быть пустым");
        }

        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            throw new InvalidData("Номер рейса не может быть пустым");
        }

        Passenger passenger = getPassengerByName(passengerName);
        Flight flight = getFlightByNumber(flightNumber);

        Booking booking = new Booking(flight, passenger);
        addBooking(booking);
        return booking;
    }
}