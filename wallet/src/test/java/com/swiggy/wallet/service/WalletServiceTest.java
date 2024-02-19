package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateWallet() {
        Long userId = 1L;
        Users user = new Users();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet wallet = walletService.createWallet(userId);

        assertNotNull(wallet);
        assertEquals(userId, wallet.getUsers().getId());
        verify(userRepository,times(1)).findById(userId);
        verify(walletRepository,times(1)).save(any(Wallet.class));
    }

    @Test
    public void testDeposit() throws InsufficientBalanceException {
        Long userId = 1L;
        Money money = new Money(new BigDecimal("100.00"), Currency.USD);
        Wallet wallet = new Wallet();
        wallet.setCurrentBalance(new Money(new BigDecimal("50.00"), Currency.USD));
        Users user = new Users();
        user.setId(userId);
        user.setWallet(wallet);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updatedWallet = walletService.deposit(userId, money);

        assertNotNull(updatedWallet);
        assertEquals(new BigDecimal("150.00"), updatedWallet.getCurrentBalance().getAmount());
        verify(userRepository,times(1)).findById(userId);
        verify(walletRepository,times(1)).save(any(Wallet.class));
    }

    @Test
    public void testWithdraw() throws InsufficientBalanceException {
        Long userId = 1L;
        Money money = new Money(new BigDecimal("30.00"), Currency.USD);
        Wallet wallet = new Wallet();
        wallet.setCurrentBalance(new Money(new BigDecimal("50.00"), Currency.USD));
        Users user = new Users();
        user.setId(userId);
        user.setWallet(wallet);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updatedWallet = walletService.withdraw(userId, money);

        assertNotNull(updatedWallet);
        assertEquals(new BigDecimal("20.00"), updatedWallet.getCurrentBalance().getAmount());
        verify(userRepository,times(1)).findById(userId);
        verify(walletRepository,times(1)).save(any(Wallet.class));
    }

    @Test
    public void testWithdrawWithInsufficientBalance() {
        Long userId = 1L;
        Money moneyToWithdraw = new Money(new BigDecimal("100.00"), Currency.USD);
        Wallet wallet = new Wallet();
        wallet.setCurrentBalance(new Money(BigDecimal.ZERO, Currency.USD));
        Users user = new Users();
        user.setId(userId);
        user.setWallet(wallet);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(InsufficientBalanceException.class, () -> walletService.withdraw(userId, moneyToWithdraw));

        verify(userRepository,times(1)).findById(userId);
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    public void testTransferMoney() throws InsufficientBalanceException {
        Users sender = new Users();
        sender.setId(1L);
        Wallet senderWallet = new Wallet(1L,new Money(new BigDecimal("100"), Currency.USD),sender);
        sender.setWallet(senderWallet);

        Users receiver = new Users();
        receiver.setId(2L);
        Wallet receiverWallet = new Wallet(receiver);
        receiver.setWallet(receiverWallet);

        Money transferAmount = new Money(new BigDecimal("50"), Currency.USD);

        when(walletRepository.save(senderWallet)).thenReturn(senderWallet);
        when(walletRepository.save(receiverWallet)).thenReturn(receiverWallet);

        TransactionResponse transactionResponse = walletService.transferMoney(sender, receiver, transferAmount);

        verify(walletRepository).save(senderWallet);
        verify(walletRepository).save(receiverWallet);
//        assertEquals(senderWallet, updatedSenderWallet);
//        assertEquals(sender.getWallet(), updatedSenderWallet);
        assertEquals(receiver.getWallet().getCurrentBalance(), new Money(new BigDecimal("50"), Currency.USD));
    }

    @Test
    public void testGetAllWallets() {
        List<Wallet> wallets = new ArrayList<>();
        wallets.add(new Wallet());
        when(walletRepository.findAll()).thenReturn(wallets);

        List<Wallet> result = walletService.getAllWallets();

        assertEquals(wallets.size(), result.size());
        verify(walletRepository,times(1)).findAll();
    }
}
