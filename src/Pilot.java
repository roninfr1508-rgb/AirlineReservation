public class Pilot extends Person {

    public Pilot(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Pilot";
    }
}
