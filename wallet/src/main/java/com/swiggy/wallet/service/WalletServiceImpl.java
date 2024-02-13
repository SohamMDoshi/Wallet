package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.WalletNotFoundException;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        wallet.setCurrentBalance(new Money(new BigDecimal("0.00"), Currency.USD));
        return walletRepository.save(wallet);
    }

    @Override
    public void deposit(Long walletId, Money money) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id "+walletId));
        wallet.deposit(money);
        walletRepository.save(wallet);
    }

    @Override
    public void withdraw(Long walletId, Money money) throws InsufficientBalanceException {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id "+walletId));
        wallet.withdraw(money);
        walletRepository.save(wallet);
    }
}
