package com.destroY17.controller;

import com.destroY17.dto.CurrencyDTO;
import com.destroY17.entity.Currency;
import com.destroY17.exception.NotFoundException;
import com.destroY17.mapper.CurrencyMapper;
import com.destroY17.service.CurrencyService;

import com.destroY17.validation.CurrencyCode;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@Validated
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return new ResponseEntity<>(currencyService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Currency> getByCode(@PathVariable @CurrencyCode String code) {
        Currency currency = currencyService.getByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency with this code not found"));
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Currency> insert(@Valid @RequestBody CurrencyDTO currencyDTO) {
        Currency insertedCurrency = currencyMapper.mapToEntity(currencyDTO);
        return new ResponseEntity<>(currencyService.insert(insertedCurrency).orElseThrow(), HttpStatus.OK);
    }
}