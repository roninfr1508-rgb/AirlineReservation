import java.util.Objects;

public class Flight {
    // Поля приватные -  Инкапсуляция
    private String number;
    private double price;

    // Конструктор
    public Flight(String number, double price) {
        this.number = number;
        // Используем сеттер для проверки цены даже при создании
        setPrice(price);
    }

    // Геттеры
    public String getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    // Сеттеры - требование "implement getters/setters"
    // + Валидация (Requirement 2: enforce encapsulation with validation)
    public void setNumber(String number) {
        this.number = number;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной!");
        }
        this.price = price;
    }

    // Требование: override toString()
    @Override
    public String toString() {
        return "Flight{" +
                "number='" + number + '\'' +
                ", price=" + price +
                '}';
    }

    // Требование: override equals() and hashCode()
    // Это нужно, чтобы Data Pool (коллекции) работал корректно
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(number, flight.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}