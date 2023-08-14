package com.destroY17.dto;

import com.destroY17.validation.CurrencyCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDTO {
    @CurrencyCode
    private String baseCurrencyCode;
    @CurrencyCode
    private String targetCurrencyCode;
    @NotNull
    @Positive
    private BigDecimal rate;
}
