package com.swiggy.wallet.Expection;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() {
        super();
    }

    public WalletNotFoundException(String msg) {
        super(msg);
    }
}
