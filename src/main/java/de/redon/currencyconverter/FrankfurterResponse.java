package de.redon.currencyconverter;

import java.util.Map;

public record FrankfurterResponse(
        double amount,
        String base,
        String date,
        Map<String, Double> rates
) {
}