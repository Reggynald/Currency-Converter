package de.redon.currencyconverter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class ConversionEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
    private double rate;
    private BigDecimal result;
    private LocalDateTime timestamp;

    protected ConversionEntry() {
    }

    public ConversionEntry(String fromCurrency, String toCurrency, BigDecimal amount, double rate, BigDecimal result) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.rate = rate;
        this.result = result;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getFromCurrency() { return fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public BigDecimal getAmount() { return amount; }
    public double getRate() { return rate; }
    public BigDecimal getResult() { return result; }
    public LocalDateTime getTimestamp() { return timestamp; }
}