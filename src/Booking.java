public class Booking {
    Flight flight;
    Passenger passenger;

    Booking(Flight flight, Passenger passenger) {
        this.flight = flight;
        this.passenger = passenger;
    }

    void printInfo() {
        System.out.println("Passenger: " + passenger.name);
        System.out.println("Flight: " + flight.number);
        System.out.println("Price: " + flight.price);
    }
}
