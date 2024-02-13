package com.swiggy.wallet.Expection;

public class InvalidAmountException extends RuntimeException{

    public InvalidAmountException(){
        super("Amount shouldn't be negative");
    }
}
