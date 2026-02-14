package com.airline.portal.repository;

import com.airline.portal.exception.InvalidData;
import com.airline.portal.exception.ResourceNotFound;
import com.airline.portal.config.DBConnection;
import com.airline.portal.domain.Flight;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightRepository {
    private static final String SELECT_BY_NUMBER = "SELECT number, price FROM flights WHERE number = ?";

    private static final String SELECT_ALL = "SELECT number, price FROM flights";
    private static final String INSERT = "INSERT INTO flights (number, price) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE flights SET price = ? WHERE number = ?";
    private static final String DELETE = "DELETE FROM flights WHERE number = ?";

    public List<Flight> findAll() throws SQLException {
        List<Flight> flights = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                Flight flight = new Flight(rs.getString("number"), rs.getDouble("price"));
                flights.add(flight);
            }
        }
        return flights;
    }

    public Flight findByNumber(String number) throws SQLException, ResourceNotFound, InvalidData {
        if (number == null || number.trim().isEmpty()) {
            throw new InvalidData("Номер рейса не может быть пустым");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NUMBER)) {

            pstmt.setString(1, number);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Flight(rs.getString("number"), rs.getDouble("price"));
                }
            }
        }
        throw new ResourceNotFound("Рейс с номером " + number + " не найден");
    }


    public void save(Flight flight) throws SQLException, InvalidData {
        if (flight == null) {
            throw new InvalidData("Рейс не может быть null");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {

            pstmt.setString(1, flight.getNumber());
            pstmt.setDouble(2, flight.getPrice());
            pstmt.executeUpdate();
        }
    }


    public void update(Flight flight) throws SQLException, InvalidData, ResourceNotFound {
        if (flight == null) {
            throw new InvalidData("Рейс не может быть null");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {

            pstmt.setDouble(1, flight.getPrice());
            pstmt.setString(2, flight.getNumber());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ResourceNotFound("Рейс не найден для обновления");
            }
        }
    }


    public void delete(String number) throws SQLException, InvalidData, ResourceNotFound {
        if (number == null || number.trim().isEmpty()) {
            throw new InvalidData("Номер рейса не может быть пустым");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {

            pstmt.setString(1, number);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ResourceNotFound("Рейс не найден для удаления");
            }
        }
    }
}