package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.repository.TransactionRepository;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TransactionResponse transferAmount(Users sender,Long senderWalletId, String receiverUsername, Long receiverWalletId, Money transferAmount) throws InsufficientBalanceException, UserNotFoundException {
        Users receiver = userRepository.findByUsername(receiverUsername).orElseThrow(()-> new UserNotFoundException(receiverUsername));
        Wallet senderWallet = sender.getWalletWithId(senderWalletId);
        Wallet receiverWallet = receiver.getWalletWithId(receiverWalletId);

        try{
            senderWallet.withdraw(transferAmount);
        }catch (InsufficientBalanceException e) {throw new InsufficientBalanceException();}
        receiverWallet.deposit(transferAmount);

        walletRepository.save(receiverWallet);
        walletRepository.save(senderWallet);
        recordTransaction(sender,receiver,transferAmount);
        return new TransactionResponse("Transferred amount successful",senderWallet.getCurrentBalance());
    }

    public void recordTransaction(Users sender, Users receiver, Money transferAmount) {
        Transaction senderTransaction = new Transaction(TransactionType.SENT, sender.getId(), receiver.getUsername(),
                transferAmount,LocalDateTime.now());
        Transaction receiverTransaction = new Transaction(TransactionType.RECEIVED, receiver.getId(), sender.getUsername(),
                transferAmount,LocalDateTime.now());
        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);
    }

    @Override
    public List<Transaction> transactionHistory(Long userId) {
        return transactionRepository.findByCurrentUser(userId);
    }

    @Override
    public List<Transaction> getTransactionHistoriesInDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsByUserIdAndTimestamp(userId, startDate,endDate);
    }

}
