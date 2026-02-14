package com.airline.portal.repository;
import com.airline.portal.config.DBConnection;
import com.airline.portal.domain.Passenger;
import com.airline.portal.exception.InvalidData;
import com.airline.portal.exception.ResourceNotFound;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerRepository {
    private static final String SELECT_ALL = "SELECT name, age FROM passengers";
    private static final String SELECT_BY_NAME = "SELECT name, age FROM passengers WHERE LOWER(name) = LOWER(?)";
    private static final String INSERT = "INSERT INTO passengers (name, age, role) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE passengers SET age = ? WHERE LOWER(name) = LOWER(?)";
    private static final String DELETE = "DELETE FROM passengers WHERE LOWER(name) = LOWER(?)";

    // ============ READ ============
    public List<Passenger> findAll() throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                Passenger passenger = new Passenger(rs.getString("name"), rs.getInt("age"));
                passengers.add(passenger);
            }
        }
        return passengers;
    }

    public Passenger findByName(String name) throws SQLException, ResourceNotFound, InvalidData {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidData("Имя пассажира не может быть пустым");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Passenger(rs.getString("name"), rs.getInt("age"));
                }
            }
        }
        throw new ResourceNotFound("Пассажир с именем " + name + " не найден в БД");
    }

    // ============ CREATE ============
    public void save(Passenger passenger) throws SQLException, InvalidData {
        if (passenger == null) {
            throw new InvalidData("Пассажир не может быть null");
        }
        if (passenger.getName() == null || passenger.getName().trim().isEmpty()) {
            throw new InvalidData("Имя пассажира не может быть пустым");
        }
        if (passenger.getAge() < 0 || passenger.getAge() > 150) {
            throw new InvalidData("Возраст должен быть от 0 до 150");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {

            pstmt.setString(1, passenger.getName());
            pstmt.setInt(2, passenger.getAge());
            pstmt.setString(3, passenger.getRole());
            pstmt.executeUpdate();
            System.out.println("✅ Пассажир сохранен в БД: " + passenger.getName());
        }
    }

    // ============ UPDATE ============
    public void update(Passenger passenger) throws SQLException, InvalidData, ResourceNotFound {
        if (passenger == null) {
            throw new InvalidData("Пассажир не может быть null");
        }
        if (passenger.getAge() < 0 || passenger.getAge() > 150) {
            throw new InvalidData("Возраст должен быть от 0 до 150");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {

            pstmt.setInt(1, passenger.getAge());
            pstmt.setString(2, passenger.getName());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ResourceNotFound("Пассажир для обновления не найден");
            }
            System.out.println("✅ Пассажир обновлен в БД: " + passenger.getName());
        }
    }

    // ============ DELETE ============
    public void delete(String name) throws SQLException, InvalidData, ResourceNotFound {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidData("Имя пассажира не может быть пустым");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {

            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ResourceNotFound("Пассажир с именем " + name + " не найден для удаления");
            }
            System.out.println("✅ Пассажир удален из БД: " + name);
        }
    }
}
