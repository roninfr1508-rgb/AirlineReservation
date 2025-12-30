import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger("Aibek", 17));
        passengers.add(new Passenger("Igor", 30));
        passengers.add(new Passenger("Daryn", 19));
        passengers.add(new Passenger("Tyler", 28));

        Passenger p3 = new Passenger("Tyler",28);
        System.out.println(p3);

        ArrayList<Person> peaple = new ArrayList<>();
        peaple.add(new Pilot("Alikhan") );
        peaple.add(new Passenger("Edil",20));

        for (Person p : peaple){
            System.out.println(p.getName()+" is a "+ p.getRole());
        }

        Flight f1 = new Flight("KC101", 15000);
        Flight f2 = new Flight("KC202", 12000);

        Passenger p1 = new Passenger("Aibek", 20);
        Passenger p2 = new Passenger("Aibek", 20);

        System.out.println(p1.equals(p2));

        for (Passenger p : passengers) {
            System.out.println(p.getName() + " - " + p.getAge());
        }
        Passenger found = findPassenger(passengers, "Daryn");

        if (found != null) {
            System.out.println("Found Passenger: " + found.getName() + ",age:" + found.getAge());
        } else {
            System.out.println("Passenger not found");
        }

        ArrayList<Passenger> adults = filterAdults(passengers);
        System.out.println("Adults:");
        for (Passenger p : adults) {
            System.out.println(p.getName() + " - " + p.getAge());
        }

        ArrayList<Identifiable> people = new ArrayList<>();
        people.add(new Passenger("Ali",40));

        for (Identifiable p : people) {
            System.out.println(p.getRole());
        }

    }


   public static Passenger findPassenger(ArrayList<Passenger> passengers, String name) {
       for (Passenger p : passengers) {
           if (p.getName().equals(name)) {
               return p;
           }
       }
       return null;
   }
   public static ArrayList<Passenger> filterAdults(ArrayList<Passenger> passengers) {
        ArrayList<Passenger> result = new ArrayList<>();

        for (Passenger p : passengers) {
            if (p.getAge() >= 18) {
                result.add(p);
            }
        }
        return result;
   }
}