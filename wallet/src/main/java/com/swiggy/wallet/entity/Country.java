package com.swiggy.wallet.entity;

import lombok.Getter;

import static com.swiggy.wallet.entity.Currency.*;

@Getter
public enum Country {

    INDIA(INR),
    USA(USD),
    EUROPE(EUR);

    private final Currency currency;

    Country(Currency currency) {
        this.currency = currency;
    }

}
