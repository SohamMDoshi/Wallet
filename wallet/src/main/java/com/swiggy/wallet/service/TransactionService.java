package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Transaction;
import com.swiggy.wallet.entity.Users;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionResponse transferAmount(Users sender,Long senderWalletId, String receiverUsername, Long receiverWalletId, Money transferAmount)
            throws InsufficientBalanceException, UserNotFoundException;

    List<Transaction> transactionHistory(Long userId);

    public List<Transaction> getTransactionHistoriesInDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
