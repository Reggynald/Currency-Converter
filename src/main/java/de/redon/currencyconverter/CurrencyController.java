package de.redon.currencyconverter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
public class CurrencyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ConversionRepository repository;

    public CurrencyController(ConversionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/convert")
    public ConversionResult convert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam BigDecimal amount
    ) {
        String fromCode = from.toUpperCase();
        String toCode = to.toUpperCase();

        double rate;
        if (fromCode.equals(toCode)) {
            rate = 1.0;
        } else {
            String url = "https://api.frankfurter.dev/v1/latest?base=" + fromCode + "&symbols=" + toCode;

            FrankfurterResponse response;
            try {
                response = restTemplate.getForObject(url, FrankfurterResponse.class);
            } catch (RestClientException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Ungueltige Waehrung: " + fromCode + " oder " + toCode
                );
            }

            if (response == null || response.rates() == null || !response.rates().containsKey(toCode)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Kein Kurs gefunden fuer " + fromCode + " nach " + toCode
                );
            }

            rate = response.rates().get(toCode);
        }

        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal result = amount
                .multiply(BigDecimal.valueOf(rate))
                .setScale(2, RoundingMode.HALF_UP);

        ConversionEntry entry = new ConversionEntry(fromCode, toCode, roundedAmount, rate, result);
        repository.save(entry);

        return new ConversionResult(fromCode, toCode, roundedAmount, rate, result);
    }

    @GetMapping("/history")
    public List<ConversionEntry> history() {
        return repository.findAll();
    }
}