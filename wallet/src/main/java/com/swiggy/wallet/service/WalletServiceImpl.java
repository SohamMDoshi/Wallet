package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.WalletNotFoundException;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        return walletRepository.save(wallet);
    }

    @Override
    public double checkBalance(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id "+walletId));
        return wallet.getCurrentBalance();
    }

    @Override
    public void deposit(Long walletId, double amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id "+walletId));
        wallet.deposit(amount);
        walletRepository.save(wallet);
    }

    @Override
    public void withdraw(Long walletId, double amount) throws InsufficientBalanceException {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id "+walletId));
        if(wallet.getCurrentBalance() < amount) throw new InsufficientBalanceException();
        wallet.withdraw(amount);
        walletRepository.save(wallet);
    }
}
