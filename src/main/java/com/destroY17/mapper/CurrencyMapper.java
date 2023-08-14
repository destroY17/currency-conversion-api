package com.destroY17.mapper;

import com.destroY17.dto.CurrencyDTO;
import com.destroY17.entity.Currency;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CurrencyMapper implements Mapper<Currency, CurrencyDTO> {
    private final ModelMapper modelMapper;

    @Override
    public Currency mapToEntity(CurrencyDTO currencyDTO) {
        return modelMapper.map(currencyDTO, Currency.class);
    }

    @Override
    public CurrencyDTO mapToDto(Currency currency) {
        return modelMapper.map(currency, CurrencyDTO.class);
    }
}
