package com.destroY17.dto;

import com.destroY17.validation.CurrencyCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {
    @CurrencyCode
    private String code;
    @NotBlank
    private String name;
    @NotBlank
    private String sign;
}
