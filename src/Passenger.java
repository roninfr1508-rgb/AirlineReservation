 class Passenger {
    private String name;
    private int age;

    public void setAge(int age){
        if(age>=0){
            this.age = age;
        }
    }
    public String getName() {
        return name;
    }
    public int getAge(){
        return age;
    }
    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
