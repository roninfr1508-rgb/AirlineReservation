package com.airline.portal.domain;
/**
 * Бронирование - связь между Flight и Passenger
 */
public class Booking {
    private Flight flight;
    private Passenger passenger;

    public Booking(Flight flight, Passenger passenger) {
        this.flight = flight;
        this.passenger = passenger;
    }

    // ✅ ДОБАВИТЬ public getters
    public Flight getFlight() {
        return flight;
    }

    public Passenger getPassenger() {
        return passenger;
    }

}
