package de.redon.currencyconverter;

import java.math.BigDecimal;

public record ConversionResult(
        String from,
        String to,
        BigDecimal amount,
        double rate,
        BigDecimal result
) {
}