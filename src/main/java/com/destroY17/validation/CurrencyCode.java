package com.destroY17.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Size(min = 3, max = 3)
@NotBlank
public @interface CurrencyCode {
    String message() default "Code must be exactly 3 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
