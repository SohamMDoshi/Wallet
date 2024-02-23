package com.swiggy.wallet.Expection;

public class SelfTransferException extends RuntimeException{

    public SelfTransferException () {
        super("Provide receiver username instead of your username");
    }
}
