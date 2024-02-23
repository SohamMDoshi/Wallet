package com.swiggy.wallet.customValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValueValidator.class)
public @interface ValidCurrencyValue {
    String message() default "Invalid currency value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

