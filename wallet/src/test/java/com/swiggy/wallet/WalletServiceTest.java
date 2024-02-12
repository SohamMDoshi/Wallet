package com.swiggy.wallet;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.WalletRepository;
import com.swiggy.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
        double balance = walletService.checkBalance(1L);
        assertEquals(0.0, balance);
    }

    @Test
    public void testDeposit() {
        Wallet wallet = new Wallet();
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        double depositAmount = 20.0;
        walletService.deposit(1L,depositAmount);
        assertEquals(20.0, wallet.getCurrentBalance());
    }

    @Test
    public void testWithdrawWhileHavingSufficientBalance() {
        Wallet wallet = new Wallet();
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        double depositAmount = 20.0;
        walletService.deposit(1L,depositAmount);
        assertEquals(20.0, wallet.getCurrentBalance());

        double withdrawAmount = 10.0;
        walletService.withdraw(1L,withdrawAmount);
        assertEquals(10.0,wallet.getCurrentBalance());
    }

    @Test
    public void testWithdrawWhileHavingInsufficientBalance() {
        Wallet wallet = new Wallet();
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        double depositAmount = 20.0;
        walletService.deposit(1L,depositAmount);
        assertEquals(20.0, wallet.getCurrentBalance());

        double withdrawAmount = 30.0;
        assertThrows(InsufficientBalanceException.class, ()-> walletService.withdraw(1L,withdrawAmount));
        assertEquals(20.0,wallet.getCurrentBalance());
    }

}
