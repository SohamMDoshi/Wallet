package com.swiggy.wallet.service;

import com.swiggy.wallet.dto.TransactionAmountDTO;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.grpcClient.CurrencyConversionClient;
import com.swiggy.wallet.repository.TransactionRepository;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import currencyconversion.ConvertResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyConversionClient conversionClient;

    @Autowired
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        transactionService = new TransactionServiceImpl(conversionClient);
        setField(transactionService, "userRepository", userRepository);
        setField(transactionService, "walletRepository", walletRepository);
        setField(transactionService, "transactionRepository", transactionRepository);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTransferAmount() throws Exception {
        // Arrange
        Users sender = new Users();
        sender.setId(1L);
        sender.setUsername("sender");

        Users receiver = new Users();
        receiver.setId(2L);
        receiver.setUsername("receiver");

        Wallet senderWallet = new Wallet();
        senderWallet.setCurrentBalance(new Money(BigDecimal.valueOf(100), Currency.USD));

        Wallet receiverWallet = new Wallet();
        receiverWallet.setCurrentBalance(new Money(BigDecimal.valueOf(50), Currency.USD));

        Money transferAmount = new Money(BigDecimal.valueOf(50), Currency.USD);

        ConvertResponse convertResponse = ConvertResponse.newBuilder()
        .setConvertedAmount(50.0)
        .setCurrency("USD")
        .setServiceFee(2.0)
        .setBaseCurrencyServiceFee(3.0)
        .build();

        when(userRepository.findByUsername("receiver")).thenReturn(Optional.of(receiver));
        when(walletRepository.findByUserId(1L, 1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByUserId(2L, 2L)).thenReturn(Optional.of(receiverWallet));
        when(conversionClient.convertCurrency("USD", "USD", 50.0)).thenReturn(convertResponse);

        TransactionResponse response = transactionService.transferAmount(sender, 1L, "receiver", 2L, transferAmount);

        assertEquals("50.00",response.getBalance().getAmount().toString());
        assertEquals("USD",response.getBalance().getCurrency().toString());
        assertEquals("Transferred amount successful",response.getResponse());
    }

    @Test
    public void testRecordTransaction() {
        // Arrange
        Users sender = new Users();
        sender.setId(1L);
        sender.setUsername("sender");

        Users receiver = new Users();
        receiver.setId(2L);
        receiver.setUsername("receiver");

        TransactionAmountDTO transferAmount = new TransactionAmountDTO(
                new Money(BigDecimal.valueOf(50), Currency.USD),
                new Money(BigDecimal.valueOf(45), Currency.USD)
        );

        // Act
        transactionService.recordTransaction(sender, receiver, transferAmount, 2.0, 3.0);

        // Assert
        // Add your assertions here
    }
}