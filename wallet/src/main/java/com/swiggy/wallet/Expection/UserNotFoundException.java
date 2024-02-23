package com.swiggy.wallet.Expection;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String username) {
        super("User not found with username"+username);
    }

    public UserNotFoundException(Long userId) {
        super("User not found with username"+userId);
    }
}
