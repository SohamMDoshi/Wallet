package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Wallet;

public interface WalletService {
    Wallet createWallet();
    void deposit(Long walletId, Money money);
    void withdraw(Long walletId, Money money) throws InsufficientBalanceException;
}
