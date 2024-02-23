package com.swiggy.wallet.service;

import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.repository.TransactionRepository;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {


    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    public void testTransferAmountSuccess() throws Exception {
        Users sender = new Users("sender", "password");
        Users receiver = new Users("receiver", "password");
        Wallet senderWallet = new Wallet(1L,new Money(BigDecimal.TEN,Currency.USD),sender);
        Wallet receiverWallet = new Wallet(2L,new Money(BigDecimal.ZERO,Currency.USD),receiver);
        sender.getWallets().add(senderWallet);
        receiver.getWallets().add(receiverWallet);

        Money transferAmount = new Money(new BigDecimal("5"), Currency.USD);

        when(userRepository.findByUsername("receiver")).thenReturn(Optional.of(receiver));
        when(walletRepository.save(senderWallet)).thenReturn(new Wallet(1L,new Money(new BigDecimal("5"),Currency.USD),sender));
        when(walletRepository.save(receiverWallet)).thenReturn(new Wallet(2L,new Money(new BigDecimal("5"),Currency.USD),receiver));

        TransactionResponse response = transactionService.transferAmount(sender,1L, "receiver",2L, transferAmount);

        assertEquals("Transferred amount successful", response.getResponse());
        assertEquals(new BigDecimal("5.00"), response.getBalance());

        verify(userRepository,times(1)).findByUsername("receiver");
    }

    @Test
    void testGetTransactionHistoriesInDateRange() {
        Users users = Mockito.mock(Users.class);

        List<Transaction> transactionHistories = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 1, 31, 23, 59);

        transactionHistories.add(new Transaction(TransactionType.SENT, users.getId(), "otherUser1", new Money(new BigDecimal("100.00"), Currency.USD),
                LocalDateTime.of(2022, 1, 5, 12, 0)));
        transactionHistories.add(new Transaction(TransactionType.RECEIVED, users.getId(),"otherUser2", new Money(new BigDecimal("50.00"),Currency.USD),
                LocalDateTime.of(2022, 1, 15, 10, 30)));
        transactionHistories.add(new Transaction(TransactionType.SENT, users.getId(),"otherUser3", new Money(new BigDecimal("200.00"),Currency.USD),
                LocalDateTime.of(2022, 1, 25, 8, 15)));
        transactionHistories.add(new Transaction(TransactionType.RECEIVED, users.getId(), "otherUser4", new Money(new BigDecimal("150.00"),Currency.USD),
                LocalDateTime.of(2021, 12, 20, 14, 45)));
        transactionHistories.add(new Transaction(TransactionType.SENT, users.getId(), "otherUser5", new Money(new BigDecimal("75.00"),Currency.USD),
                LocalDateTime.of(2022, 2, 10, 16, 20)));

        List<Transaction> expectedTransactionHistories = new ArrayList<>(transactionHistories.subList(0, 3));

        when(transactionRepository.findTransactionsByUserIdAndTimestamp(users.getId(),startDate,endDate)).thenReturn(expectedTransactionHistories);

        List<Transaction> result = transactionService.getTransactionHistoriesInDateRange(users.getId(), startDate, endDate);

        assertEquals(3, result.size());
    }
}
