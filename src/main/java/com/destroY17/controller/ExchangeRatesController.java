package com.destroY17.controller;

import com.destroY17.dto.ExchangeRateDTO;
import com.destroY17.dto.RateDTO;
import com.destroY17.entity.ExchangeRate;
import com.destroY17.exception.NotFoundException;
import com.destroY17.mapper.ExchangeRateMapper;
import com.destroY17.service.ExchangeRateService;
import com.destroY17.validation.CurrencyCode;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exchangeRates")
@Validated
@RequiredArgsConstructor
public class ExchangeRatesController {
    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateMapper exchangeRateMapper;

    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAll() {
        return new ResponseEntity<>(exchangeRateService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{from}/{to}")
    public ResponseEntity<ExchangeRate> getByCodes(@PathVariable @CurrencyCode String from,
                                                   @PathVariable @CurrencyCode String to) {
        ExchangeRate exchangeRate = exchangeRateService.getByCodes(from, to)
                .orElseThrow(() -> new NotFoundException("Exchange rate not found"));
        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ExchangeRate> insert(@Valid @RequestBody ExchangeRateDTO exchangeRateDTO) {
        ExchangeRate insertedExchangeRate = exchangeRateMapper.mapToEntity(exchangeRateDTO);
        return new ResponseEntity<>(exchangeRateService.insert(insertedExchangeRate), HttpStatus.OK);
    }

    @PatchMapping("/{from}/{to}")
    public ResponseEntity<ExchangeRate> update(@PathVariable @CurrencyCode String from,
                                               @PathVariable @CurrencyCode String to,
                                               @Valid @RequestBody RateDTO rateDTO) {
        ExchangeRate updatedExchangeRate = exchangeRateService.getByCodes(from, to)
                .orElseThrow(() -> new NotFoundException("Exchange rate not found"));
        updatedExchangeRate.setRate(rateDTO.getRate());
        return new ResponseEntity<>(exchangeRateService.update(updatedExchangeRate), HttpStatus.OK);
    }
}