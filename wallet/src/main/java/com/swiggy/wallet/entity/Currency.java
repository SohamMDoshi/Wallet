package com.swiggy.wallet.entity;

import lombok.Getter;

import java.math.BigDecimal;


@Getter
public enum Currency {
    USD(BigDecimal.ONE),
    INR(new BigDecimal("0.13")),
    EUR(new BigDecimal("1.2")),
    GBP(new BigDecimal("1.38"));
    
    
    private final BigDecimal conversionRateTOUSD;
    
    Currency(BigDecimal conversionRateTOUSD){
        this.conversionRateTOUSD = conversionRateTOUSD;
    }

}
