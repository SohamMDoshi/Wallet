package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.Expection.WalletNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Wallet createWallet(Long userID) {
        Users users = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("Users not found with id "+ userID));
        Wallet wallet = new Wallet(users);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet deposit(Long userID, Money money) {
        Users users = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("Users not found with id "+ userID));
        Wallet wallet = users.getWallet();
        if (wallet == null) throw new WalletNotFoundException("Wallet not found for users with id: " + userID);
        wallet.deposit(money);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet withdraw(Long userID, Money money) throws InsufficientBalanceException {
        Users users = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("Users not found with id "+ userID));
        Wallet wallet = users.getWallet();
        if (wallet == null) throw new WalletNotFoundException("Wallet not found for users with id: " + userID);
        wallet.withdraw(money);
        return walletRepository.save(wallet);
    }

    @Override
    public List<Wallet> getAllWallets() {
        List<Wallet> walletList = walletRepository.findAll();
        return walletList;
    }

    @Override
    public TransactionResponse transferMoney(Users sender, Users receiver, Money transferAmount) throws InsufficientBalanceException {
        Wallet senderWallet = sender.getWallet();
        try{
            senderWallet.withdraw(transferAmount);
        }catch (InsufficientBalanceException e) {throw new InsufficientBalanceException();}
        Wallet receiverWallet = receiver.getWallet();
        receiverWallet.deposit(transferAmount);
        walletRepository.save(receiverWallet);
        walletRepository.save(senderWallet);
        return new TransactionResponse("Transferred amount successful",senderWallet.getCurrentBalance().getAmount());
    }
}
