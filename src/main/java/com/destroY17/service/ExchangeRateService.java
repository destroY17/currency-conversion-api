package com.destroY17.service;

import com.destroY17.dto.ExchangeDTO;
import com.destroY17.entity.ExchangeRate;
import com.destroY17.exception.ExchangeException;
import com.destroY17.repository.ExchangeRateRepository;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public List<ExchangeRate> getAll() {
        return exchangeRateRepository.getAll();
    }

    public Optional<ExchangeRate> getByCodes(String from, String to) {
        return exchangeRateRepository.getByCodes(from, to);
    }

    public ExchangeRate insert(ExchangeRate entity) {
        return exchangeRateRepository.save(entity);
    }

    public ExchangeRate update(ExchangeRate entity) {
        return exchangeRateRepository.update(entity);
    }

    public ExchangeDTO exchange(String baseCurrencyCode, String targetCurrencyCode, @Positive BigDecimal amount) {
        var exchangeRate = getExchangeRate(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new ExchangeException("Exchange is impossible"));
        return new ExchangeDTO(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate(),
                amount,
                amount.multiply(exchangeRate.getRate())
        );
    }

    private Optional<ExchangeRate> getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        var exchangeRate = getDirectExchangeRate(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate.isEmpty()) {
            exchangeRate = getTransitiveExchangeRateThroughUsd(baseCurrencyCode, targetCurrencyCode);
        }
        return exchangeRate;
    }

    private Optional<ExchangeRate> getDirectExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        var exchangeRate = getByCodes(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate.isEmpty()) {
            exchangeRate = getByCodes(targetCurrencyCode, baseCurrencyCode);
            if (exchangeRate.isPresent()) {
                return Optional.of(getReverseExchangeRate(exchangeRate.get()));
            }
        }
        return exchangeRate;
    }

    private ExchangeRate getReverseExchangeRate(@NonNull ExchangeRate exchangeRate) {
        return new ExchangeRate(
                exchangeRate.getTargetCurrency(),
                exchangeRate.getBaseCurrency(),
                getReverseRate(exchangeRate.getRate())
        );
    }

    private Optional<ExchangeRate> getTransitiveExchangeRate(String  transitCurrencyCode, String baseCurrencyCode,
                                                             String targetCurrencyCode) {
        var exchangeBase = exchangeRateRepository.getByCodes(transitCurrencyCode, baseCurrencyCode);
        var exchangeTarget = exchangeRateRepository.getByCodes(transitCurrencyCode, targetCurrencyCode);

        if (exchangeBase.isEmpty() || exchangeTarget.isEmpty())
            return Optional.empty();

        var baseToTransitRate = getReverseRate(exchangeBase.get().getRate());
        var transitToTargetRate = exchangeTarget.get().getRate();
        var baseToTargetRate = baseToTransitRate.multiply(transitToTargetRate);

        var exchangeRate = new ExchangeRate(
                exchangeBase.get().getTargetCurrency(),
                exchangeTarget.get().getTargetCurrency(),
                baseToTargetRate);

        return Optional.of(exchangeRate);
    }

    private BigDecimal getReverseRate(BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, 2, RoundingMode.FLOOR);
    }

    private Optional<ExchangeRate> getTransitiveExchangeRateThroughUsd(String baseCurrencyCode, String targetCurrencyCode) {
        final String transit = "USD";
        return getTransitiveExchangeRate(transit, baseCurrencyCode, targetCurrencyCode);
    }
}
