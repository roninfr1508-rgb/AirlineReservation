public class Main {
    public static void main(String[] args) {

        Flight f1 = new Flight("KC101", 15000);
        Flight f2 = new Flight("KC202", 12000);

        Passenger p1 = new Passenger("Aibek");
        Passenger p2 = new Passenger("Dias");

        Booking b1 = new Booking(f1, p1);
        Booking b2 = new Booking(f2, p2);

        b1.printInfo();
        System.out.println();
        b2.printInfo();

    }
}