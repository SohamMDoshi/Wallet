package com.swiggy.wallet.Expection;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(String msg) {
        super(msg);
    }

    public WalletNotFoundException(Long walletId, String username) {

        super("Wallet with id "+walletId +" does not belong to "+username);
    }

}
