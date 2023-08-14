package com.destroY17.service;

import com.destroY17.entity.Currency;
import com.destroY17.repository.CurrencyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    public List<Currency> getAll() {
        return currencyRepository.getAll();
    }

    public Optional<Currency> getByCode(String code) {
        return currencyRepository.getByCode(code);
    }

    public Optional<Currency> insert(Currency entity) {
        return Optional.ofNullable(currencyRepository.save(entity));
    }
}
