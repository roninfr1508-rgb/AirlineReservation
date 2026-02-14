package com.airline.portal.service;
import com.airline.portal.domain.Flight;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generics + Lambda
 */
public class FlightFilter {

    // GENERICS - <T> работает с любым типом
    public static <T> int countItems(List<T> items) {
        return items.size();
    }

    // LAMBDA - фильтр по цене
    public static List<Flight> getCheapFlights(List<Flight> flights) {
        return flights.stream()
                .filter(f -> f.getPrice() < 200)  // ✅ LAMBDA
                .collect(Collectors.toList());
    }

    // LAMBDA - сортировка
    public static List<Flight> sortByPrice(List<Flight> flights) {
        return flights.stream()
                .sorted((f1, f2) -> Double.compare(f1.getPrice(), f2.getPrice()))  //  LAMBDA
                .collect(Collectors.toList());
    }
}