package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.entity.Wallet;

public interface WalletService {
    Wallet createWallet();
    double checkBalance(Long walletId);
    void deposit(Long walletId, double amount);
    void withdraw(Long walletId, double amount) throws InsufficientBalanceException;
}
