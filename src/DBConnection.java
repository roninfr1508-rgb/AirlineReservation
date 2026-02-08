import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/Airline_OOP";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0000";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ============ FLIGHT OPERATIONS ============

    /**
     * Сохранить рейс в БД
     */
    public void saveFlight(Flight flight) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO flights (number, price) VALUES (?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, flight.getNumber());
                pstmt.setDouble(2, flight.getPrice());
                pstmt.executeUpdate();
                System.out.println("✅ Рейс сохранен в БД: " + flight.getNumber());
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при сохранении рейса: " + e.getMessage());
        }
    }

    /**
     * Обновить рейс в БД
     */
    public void updateFlight(Flight flight) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE flights SET price = ? WHERE number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, flight.getPrice());
                pstmt.setString(2, flight.getNumber());
                pstmt.executeUpdate();
                System.out.println("✅ Рейс обновлен в БД: " + flight.getNumber());
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при обновлении рейса: " + e.getMessage());
        }
    }

    /**
     * Удалить рейс из БД
     */
    public void deleteFlight(String flightNumber) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM flights WHERE number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, flightNumber);
                pstmt.executeUpdate();
                System.out.println("✅ Р��йс удален из БД: " + flightNumber);
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при удалении рейса: " + e.getMessage());
        }
    }

    // ============ BOOKING OPERATIONS ============

    /**
     * Сохранить бронирование в БД
     */
    public void saveBooking(Booking booking) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO bookings (passenger_name, flight_number, price) VALUES (?, ?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, booking.passenger.getName());
                pstmt.setString(2, booking.flight.getNumber());
                pstmt.setDouble(3, booking.flight.getPrice());
                pstmt.executeUpdate();
                System.out.println("✅ Бронирование сохранено в БД");
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при сохранении бронирования: " + e.getMessage());
        }
    }

    /**
     * Обновить бронирование в БД
     */
    public void updateBooking(Booking booking) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE bookings SET price = ? WHERE passenger_name = ? AND flight_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, booking.flight.getPrice());
                pstmt.setString(2, booking.passenger.getName());
                pstmt.setString(3, booking.flight.getNumber());
                pstmt.executeUpdate();
                System.out.println("✅ Бронирование обновлено в БД");
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при обновлении бронирования: " + e.getMessage());
        }
    }

    /**
     * Удалить бронирование из БД
     */
    public void deleteBooking(String bookingId) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM bookings WHERE id = ? OR (passenger_name || '-' || flight_number) = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, bookingId);
                pstmt.setString(2, bookingId);
                pstmt.executeUpdate();
                System.out.println("✅ Бронирование удалено из БД");
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при удалении бронирования: " + e.getMessage());
        }
    }

    // ============ PASSENGER OPERATIONS ============

    /**
     * Сохранить пассажира в БД
     */
    public void savePassenger(Passenger passenger) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO passengers (name, age, role) VALUES (?, ?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, passenger.getName());
                pstmt.setInt(2, passenger.getAge());
                pstmt.setString(3, passenger.getRole());
                pstmt.executeUpdate();
                System.out.println("✅ Пассажир сохранен в БД: " + passenger.getName());
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при сохранении пассажира: " + e.getMessage());
        }
    }

    /**
     * Обновить пассажира в БД
     */
    public void updatePassenger(Passenger passenger) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE passengers SET age = ?, role = ? WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, passenger.getAge());
                pstmt.setString(2, passenger.getRole());
                pstmt.setString(3, passenger.getName());
                pstmt.executeUpdate();
                System.out.println("✅ Пассажир обновлен в БД: " + passenger.getName());
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при обновлении пассажира: " + e.getMessage());
        }
    }

    /**
     * Удалить пассажира из БД
     */
    public void deletePassenger(String passengerName) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM passengers WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, passengerName);
                pstmt.executeUpdate();
                System.out.println("✅ Пассажир удален из БД: " + passengerName);
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при удалении пассажира: " + e.getMessage());
        }
    }
}