import java.util.ArrayList;
import java.sql.Connection;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger("Aibek", 17));
        passengers.add(new Passenger("Igor", 30));
        passengers.add(new Passenger("Daryn", 19));
        passengers.add(new Passenger("Tayler", 28));

        Passenger p3 = new Passenger("Tayler",28);
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
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connected to database!");

            // Insert flight
            String insertFlight = "INSERT INTO flights (flight_code, price) VALUES (?, ?)";
            PreparedStatement psFlight = conn.prepareStatement(insertFlight);
            psFlight.setString(1, "KC101");
            psFlight.setInt(2, 15000);
            psFlight.executeUpdate();

            // Insert passenger
            String insertPassenger = "INSERT INTO passengers (name, age) VALUES (?, ?)";
            PreparedStatement psPassenger = conn.prepareStatement(insertPassenger);
            psPassenger.setString(1, "Aibek");
            psPassenger.setInt(2, 17);
            psPassenger.executeUpdate();

            // Read flights
            ResultSet rsFlights = conn.createStatement().executeQuery("SELECT * FROM flights");
            while (rsFlights.next()) {
                System.out.println(rsFlights.getInt("flight_id") + " "
                        + rsFlights.getString("flight_code") + " "
                        + rsFlights.getInt("price"));
            }

            // Read passengers
            ResultSet rsPassengers = conn.createStatement().executeQuery("SELECT * FROM passengers");
            while (rsPassengers.next()) {
                System.out.println(rsPassengers.getInt("passenger_id") + " "
                        + rsPassengers.getString("name") + " "
                        + rsPassengers.getInt("age"));
            }

            // Update
            String updateFlight = "UPDATE flights SET price = ? WHERE flight_id = ?";
            PreparedStatement psUpdate = conn.prepareStatement(updateFlight);
            psUpdate.setInt(1, 17000);
            psUpdate.setInt(2, 1); // обновляем первый рейс
            psUpdate.executeUpdate();

            //Delete
            String deletePassenger = "DELETE FROM passengers WHERE passenger_id = ?";
            PreparedStatement psDelete = conn.prepareStatement(deletePassenger);
            psDelete.setInt(1, 1); // удаляем первого пассажира
            psDelete.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
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