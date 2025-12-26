public class Booking {
    Flight flight;
    Passenger passenger;

    Booking(Flight flight, Passenger passenger) {
        this.flight = flight;
        this.passenger = passenger;
    }

    void printInfo() {
        System.out.println("Passenger: " + passenger.getName());
        System.out.println("Flight: " + flight.getNumber());
        System.out.println("Price: " + flight.getPrice());
        System.out.println("Age: " + passenger.getAge());
    }
}
