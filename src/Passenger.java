 public class Passenger extends Person  {
    private int age;
    public Passenger(String name, int age) {
        super(name);
        this.age = age;
    }

    public void setAge(int age){
        if(age>=0){
            this.age = age;
        }
    }
    public int getAge(){
        return age;
    }
    public String getRole(){
        return "Passenger";
    }
    public String toString(){
        return "Passenger {name="+ getName() + ",age:"+ getAge()+ "}";
    }

     public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         Passenger passenger = (Passenger) o;

         if (age != passenger.age) return false;
         return getName().equals(passenger.getName());
     }
     public int hashCode() {
         int result = getName().hashCode();
         result = 31 * result + age;
         return result;
     }
}
