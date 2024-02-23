package com.swiggy.wallet.entity;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
public enum Currency {
    USD(BigDecimal.ONE),
    INR(new BigDecimal("82.88")),
    EUR(new BigDecimal("0.92")),
    GBP(new BigDecimal("0.78"));


    private final BigDecimal conversionRateToUSD;

    Currency(BigDecimal conversionRateToUSD){
        this.conversionRateToUSD = conversionRateToUSD;
    }

    public BigDecimal convertToBase(BigDecimal amount, Currency baseCurrency) {
        // Convert the amount from the currency to USD first
        BigDecimal amountInUSD = amount.divide(conversionRateToUSD, 2, RoundingMode.HALF_UP);

        // Convert the amount from USD to the base currency
        return amountInUSD.multiply(baseCurrency.conversionRateToUSD).setScale(2, RoundingMode.HALF_UP);
    }


}
