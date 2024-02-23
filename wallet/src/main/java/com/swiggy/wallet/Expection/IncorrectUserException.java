package com.swiggy.wallet.Expection;

public class IncorrectUserException extends RuntimeException{

    public IncorrectUserException(Long id, String username) {
        super("Id : "+ id +" is not belong to "+ username);
    }
}
