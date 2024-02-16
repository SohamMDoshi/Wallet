package com.swiggy.wallet.Expection;

public class OperationNotPossible extends RuntimeException{

    public OperationNotPossible() {
        super("Operation not possible");
    }
}
