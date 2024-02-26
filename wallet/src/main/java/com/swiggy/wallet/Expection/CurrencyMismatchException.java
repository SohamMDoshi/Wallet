package com.swiggy.wallet.Expection;

public class CurrencyMismatchException extends RuntimeException{

    public CurrencyMismatchException(String baseCurrency) {
        super("user should only use base currency "+baseCurrency);
    }
}
