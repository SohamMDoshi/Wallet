package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.WalletNotFoundException;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Wallet;

import java.util.List;

public interface WalletService {
    Wallet createWallet(Long userID);
    Wallet deposit(Long userID, Money money);
    Wallet withdraw(Long userID, Money money) throws InsufficientBalanceException;
    List<Wallet> getAllWallets() throws WalletNotFoundException;
}
