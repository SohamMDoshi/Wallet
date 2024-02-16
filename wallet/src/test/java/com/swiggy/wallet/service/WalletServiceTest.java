package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.InvalidAmountException;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    public void testCreatingWalletAndCheckingCurrentBalance() {
        Wallet wallet = new Wallet();
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        assertEquals(new Money(new BigDecimal("0.00"),Currency.USD), wallet.getCurrentBalance());
    }

    @Test
    public void testDeposit() {
        Wallet wallet = new Wallet();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        Money depositAmount = new Money(new BigDecimal("5.0"), Currency.USD);
        walletService.deposit(1L,depositAmount);
        assertEquals(depositAmount, wallet.getCurrentBalance());
    }

    @Test
    public void testWithdrawWhileHavingSufficientBalance() {
        Wallet wallet = new Wallet();
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        Money depositAmount = new Money(new BigDecimal("20.0"), Currency.USD);
        walletService.deposit(1L,depositAmount);
        assertEquals(depositAmount, wallet.getCurrentBalance());

        Money withdrawAmount = new Money(new BigDecimal("5.0"), Currency.USD);
        walletService.withdraw(1L,withdrawAmount);
        Money expectedBalance = new Money(new BigDecimal("15"),Currency.USD);
        assertEquals(expectedBalance,wallet.getCurrentBalance());
    }
//
    @Test
    public void testWithdrawWhileHavingInsufficientBalance() {
        Wallet wallet = new Wallet();
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        Money withdrawAmount = new Money(new BigDecimal("5.0"), Currency.USD);
        assertThrows(InsufficientBalanceException.class, ()-> walletService.withdraw(1L,withdrawAmount));
    }

    @Test
    void testGetAllWalletsWhenNotEmpty() throws InvalidAmountException {
        List<Wallet> mockWalletList = new ArrayList<>();
        mockWalletList.add(new Wallet(1L, new Money(new BigDecimal("10"), Currency.USD),any(Users.class)));
        mockWalletList.add(new Wallet(2L, new Money(new BigDecimal("5"), Currency.USD),any(Users.class)));
        when(walletRepository.findAll()).thenReturn(mockWalletList);

        List<Wallet> result = walletService.getAllWallets();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(new Money(new BigDecimal("10"), Currency.USD), result.get(0).getCurrentBalance());
        assertEquals(2L, result.get(1).getId());
        assertEquals(new Money(new BigDecimal("5"), Currency.USD), result.get(1).getCurrentBalance());
    }

    @Test
    void testGetAllWalletsWhenItIsEmpty() {
        List<Wallet> result = walletService.getAllWallets();
        assertTrue(result.isEmpty());
    }

}
