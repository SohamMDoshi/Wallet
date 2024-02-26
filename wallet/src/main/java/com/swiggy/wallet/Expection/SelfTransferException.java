package com.swiggy.wallet.Expection;

public class SelfTransferException extends RuntimeException{

    public SelfTransferException () {
        super("User cannot transfer amount to same wallet");
    }
}
