package com.airline.portal.domain;

import java.util.Objects;

/**
 *  интерфейс IFlight - DIP + OCP
 *
 */
public class Flight implements IFlight {
    private String number;
    private double price;

    public Flight(String number, double price) {
        this.number = number;
        setPrice(price);
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной!");
        }
        this.price = price;
    }

    /**
     * Новый метод из интерфейса
     */
    @Override
    public boolean isAvailable() {
        return price > 0;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "number='" + number + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(number, flight.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}