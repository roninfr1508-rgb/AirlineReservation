package com.airline.portal.domain;
/**
 *  Интерфейс для рейсов - DIP
 * Позволяет легко создавать разные типы рейсов
 */
public interface IFlight {
    String getNumber();
    double getPrice();
    default boolean isCheap() {
        return getPrice() < 200;
    }
    void setNumber(String number);
    void setPrice(double price);
    boolean isAvailable();
}
