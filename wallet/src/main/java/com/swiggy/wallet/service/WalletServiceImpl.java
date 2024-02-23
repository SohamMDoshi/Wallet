package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.Expection.WalletNotFoundException;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Wallet createWallet(Long userID) {
        Users user = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("Users not found with id "+ userID));
        Wallet wallet = new Wallet(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet deposit(Long walletId, Money money) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() ->
                new WalletNotFoundException("Wallet not found with id: " + walletId));
        wallet.deposit(money);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet withdraw(Long walletId, Money money) throws InsufficientBalanceException {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() ->
                new WalletNotFoundException("Wallet not found for users with id: " + walletId));
        try {
            wallet.withdraw(money);
        }catch (InsufficientBalanceException e) {throw new InsufficientBalanceException();}
        return walletRepository.save(wallet);
    }

    @Override
    public Set<Wallet> getAllWallets(Long userId) {
        return walletRepository.findAllWallets(userId);
    }


}
