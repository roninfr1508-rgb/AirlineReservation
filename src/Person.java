public abstract class Person implements Identifiable {

    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public abstract String getRole();

    public String getName() {
        return name;
    }

}

