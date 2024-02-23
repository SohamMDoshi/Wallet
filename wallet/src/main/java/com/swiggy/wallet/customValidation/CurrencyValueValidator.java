package com.swiggy.wallet.customValidation;

import com.swiggy.wallet.entity.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyValueValidator implements ConstraintValidator<ValidCurrencyValue, Currency> {
    @Override
    public boolean isValid(Currency value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        for (Currency currency : Currency.values()) {
            if (value == currency) {
                return true;
            }
        }
        return false;
    }
}
