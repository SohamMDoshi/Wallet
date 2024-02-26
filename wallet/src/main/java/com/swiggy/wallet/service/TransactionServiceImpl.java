package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.CurrencyMismatchException;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.Expection.WalletNotFoundException;
import com.swiggy.wallet.dto.TransactionAmountDTO;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.grpcClient.CurrencyConversionClient;
import com.swiggy.wallet.repository.TransactionRepository;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import currencyconversion.ConvertResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;


    @Autowired
    private TransactionRepository transactionRepository;

    private final CurrencyConversionClient conversionClient;

    public TransactionServiceImpl(CurrencyConversionClient conversionClient) {
        this.conversionClient = conversionClient;
    }


    @Override
    public TransactionResponse transferAmount(Users sender, Long senderWalletId, String receiverUsername, Long receiverWalletId, Money transferAmount) throws InsufficientBalanceException, UserNotFoundException {
        Users receiver = userRepository.findByUsername(receiverUsername).orElseThrow(()-> new UserNotFoundException(receiverUsername));
        Wallet senderWallet = walletRepository.findByUserId(senderWalletId,sender.getId())
                .orElseThrow(()-> new WalletNotFoundException(senderWalletId,sender.getUsername()));
        if(!senderWallet.getCurrentBalance().getCurrency().equals(transferAmount.getCurrency()))
            throw new CurrencyMismatchException(senderWallet.getCurrentBalance().getCurrency().toString());
        Wallet receiverWallet = walletRepository.findByUserId(receiverWalletId,receiver.getId())
                .orElseThrow(()-> new WalletNotFoundException(receiverWalletId,receiverUsername));

        try{
            senderWallet.withdraw(transferAmount);
        }catch (InsufficientBalanceException e) {throw new InsufficientBalanceException();}

        if(!transferAmount.getCurrency().equals(receiverWallet.getCurrentBalance().getCurrency())) {
            ConvertResponse response = conversionClient.convertCurrency(transferAmount.getCurrency().toString(), receiverWallet.getCurrentBalance().getCurrency().toString(),
                    Double.parseDouble(transferAmount.getAmount().toString()));
            Money convertedAmount = new Money(BigDecimal.valueOf(response.getConvertedAmount()),Currency.valueOf(response.getCurrency()));
            receiverWallet.deposit(convertedAmount);
            TransactionAmountDTO amountDTO = new TransactionAmountDTO(transferAmount,convertedAmount);
            recordTransaction(sender,receiver,amountDTO,response.getServiceFee(),response.getBaseCurrencyServiceFee());
        }
        else {
            receiverWallet.deposit(transferAmount);
            TransactionAmountDTO amountDTO = new TransactionAmountDTO(transferAmount,transferAmount);
            recordTransaction(sender,receiver,amountDTO,null,null);
        }

        walletRepository.save(receiverWallet);
        walletRepository.save(senderWallet);
        return new TransactionResponse("Transferred amount successful",senderWallet.getCurrentBalance());
    }


    public void recordTransaction(Users sender, Users receiver, TransactionAmountDTO transferAmount, Double serviceFee, Double serviceFeeInBaseCurrency) {
        Transaction senderTransaction = new Transaction(TransactionType.SENT, sender.getId(), receiver.getUsername(),
                transferAmount.getSenderAmount(),LocalDateTime.now(),serviceFeeInBaseCurrency);
        Transaction receiverTransaction = new Transaction(TransactionType.RECEIVED, receiver.getId(), sender.getUsername(),
                transferAmount.getReceiverAmount(),LocalDateTime.now(),serviceFee);
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
