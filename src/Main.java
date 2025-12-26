public class Main {
    public static void main(String[] args) {

        Flight f1 = new Flight("KC101", 15000);
        Flight f2 = new Flight("KC202", 12000);

        Passenger p1 = new Passenger("Aibek",17);
        Passenger p2 = new Passenger("Dias",18);

        Booking b1 = new Booking(f1, p1);
        Booking b2 = new Booking(f2, p2);

        b1.printInfo();
        System.out.println();
        b2.printInfo();
        if (f1.getPrice() > f2.getPrice()) {
            System.out.println("F2 is cheaper");
        }else {System.out.println("F1 is cheaper");}
         //test update
    }
}