package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.*;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if(!wallet.getCurrentBalance().getCurrency().equals(money.getCurrency()))
            throw new CurrencyMismatchException(wallet.getCurrentBalance().getCurrency().toString());
        wallet.deposit(money);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet withdraw(Long walletId, Money money) throws InsufficientBalanceException {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() ->
                new WalletNotFoundException("Wallet not found for users with id: " + walletId));
        if(!wallet.getCurrentBalance().getCurrency().equals(money.getCurrency()))
            throw new CurrencyMismatchException(wallet.getCurrentBalance().getCurrency().toString());
        try {
            wallet.withdraw(money);
        }catch (InsufficientBalanceException e) {throw new InsufficientBalanceException();}
        return walletRepository.save(wallet);
    }

    @Override
    public Set<Wallet> getAllWallets(Long userId) {
        return walletRepository.findAllWallets(userId);
    }

    @Override
    public void transferAmount(Wallet senderWallet,Wallet receiverWallet, Money transferAmount) {
       try{
           senderWallet.withdraw(transferAmount);
       }catch (InsufficientBalanceException e) {throw new InsufficientBalanceException();}
       receiverWallet.deposit(transferAmount);
    }


}
