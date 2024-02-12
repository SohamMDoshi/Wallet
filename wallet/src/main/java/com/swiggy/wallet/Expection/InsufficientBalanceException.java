package com.swiggy.wallet.Expection;

public class InsufficientBalanceException extends RuntimeException{

    public InsufficientBalanceException() {
        super("Insufficient Balance");
    }
}
