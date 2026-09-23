package de.redon.currencyconverter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversionRepository extends JpaRepository<ConversionEntry, Long> {
}