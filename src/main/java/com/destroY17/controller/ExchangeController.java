package com.destroY17.controller;

import com.destroY17.dto.ExchangeDTO;
import com.destroY17.service.ExchangeRateService;
import com.destroY17.validation.CurrencyCode;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Validated
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping(path = "/exchange")
    public ResponseEntity<ExchangeDTO> getExchange(@RequestParam @CurrencyCode String from,
                                                   @RequestParam @CurrencyCode String to,
                                                   @RequestParam @Positive BigDecimal amount) {
        ExchangeDTO exchangeInfo = exchangeRateService.exchange(from, to, amount);
        return new ResponseEntity<>(exchangeInfo, HttpStatus.OK);
    }
}
