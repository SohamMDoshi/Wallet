package com.swiggy.wallet.Expection;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
