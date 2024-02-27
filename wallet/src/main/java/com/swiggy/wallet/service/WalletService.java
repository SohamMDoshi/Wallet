package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.WalletNotFoundException;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Wallet;

import java.util.Set;

public interface WalletService {
    Wallet createWallet(Long userID);
    Wallet deposit(Long walletId, Money money);
    Wallet withdraw(Long walletId, Money money) throws InsufficientBalanceException;
    Set<Wallet> getAllWallets(Long userId) throws WalletNotFoundException;

    void transferAmount(Wallet senderWallet, Wallet receiverWallet, Money transferAmount);
}