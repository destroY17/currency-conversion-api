package com.destroY17.mapper;

import com.destroY17.dto.ExchangeRateDTO;
import com.destroY17.entity.ExchangeRate;
import com.destroY17.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ExchangeRateMapper implements Mapper<ExchangeRate, ExchangeRateDTO> {
    private final ModelMapper modelMapper;
    private final CurrencyService currencyService;

    @Override
    public ExchangeRate mapToEntity(ExchangeRateDTO exchangeRateDTO) {
        return Objects.isNull(exchangeRateDTO) ?
                null :
                new ExchangeRate(
                        0,
                        currencyService.getByCode(exchangeRateDTO.getBaseCurrencyCode()).orElseThrow(),
                        currencyService.getByCode(exchangeRateDTO.getTargetCurrencyCode()).orElseThrow(),
                        exchangeRateDTO.getRate()
                );
    }

    @Override
    public ExchangeRateDTO mapToDto(ExchangeRate exchangeRate) {
        return modelMapper.map(exchangeRate, ExchangeRateDTO.class);
    }
}
