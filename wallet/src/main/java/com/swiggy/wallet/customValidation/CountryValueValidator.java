package com.swiggy.wallet.customValidation;

import com.swiggy.wallet.entity.Country;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CountryValueValidator implements ConstraintValidator<ValidCountryValue, Country> {
    @Override
    public boolean isValid(Country value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }

        for (Country country : Country.values()) {
            if (value == country) {
                return true;
            }
        }
        return false;
    }
}
