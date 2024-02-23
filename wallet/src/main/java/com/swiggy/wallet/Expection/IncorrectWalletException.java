package com.swiggy.wallet.Expection;

public class IncorrectWalletException extends RuntimeException{

    public IncorrectWalletException(Long walletId, String username) {
        super("Wallet with this id : " +walletId +  " doesn't belong to "+username);
    }
}
