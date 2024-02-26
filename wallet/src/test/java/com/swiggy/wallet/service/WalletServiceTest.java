package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Users sender;

    @Mock
    private Users receiver;

    @Mock
    private Wallet senderWallet;

    @Mock
    private Wallet receiverWallet;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateWallet() {
        Users user = new Users("testUser","password",Country.USA);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet wallet = walletService.createWallet(1L);

        assertNotNull(wallet);
        verify(userRepository, times(1)).findById(anyLong());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    public void testDeposit() {
        Money money = new Money(new BigDecimal("100.00"), Currency.USD);
        Wallet wallet = new Wallet();
        wallet.setCurrentBalance(new Money(new BigDecimal("50.00"), Currency.USD));

        when(walletRepository.findById(anyLong())).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updatedWallet = walletService.deposit(1L, money);

        assertNotNull(updatedWallet);
        assertEquals(new BigDecimal("150.00"), updatedWallet.getCurrentBalance().getAmount());
        verify(walletRepository, times(1)).findById(anyLong());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    public void testWithdraw() throws InsufficientBalanceException {
        Money withdrawAmount = new Money(new BigDecimal("100.00"), Currency.USD);
        Wallet wallet = new Wallet();
        wallet.setCurrentBalance(new Money(new BigDecimal("150.00"), Currency.USD));

        when(walletRepository.findById(anyLong())).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updatedWallet = walletService.withdraw(1L, withdrawAmount);

        assertNotNull(updatedWallet);
        assertEquals(new BigDecimal("50.00"), updatedWallet.getCurrentBalance().getAmount());
        verify(walletRepository, times(1)).findById(anyLong());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    public void testWithdrawWithInsufficientBalance() {
        Money withdrawAmount = new Money(new BigDecimal("100.00"), Currency.USD);
        Wallet wallet = new Wallet();
        wallet.setCurrentBalance(new Money(new BigDecimal("50.00"), Currency.USD));

        when(walletRepository.findById(anyLong())).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(InsufficientBalanceException.class, () -> walletService.withdraw(1L, withdrawAmount));

        verify(walletRepository, times(1)).findById(anyLong());
        verify(walletRepository, never()).save(any(Wallet.class));
    }


    @Test
    public void testGetAllWallets() {
        Set<Wallet> wallets = new HashSet<>();
        wallets.add(new Wallet());
        when(walletRepository.findAllWallets(anyLong())).thenReturn(wallets);

        Set<Wallet> result = walletService.getAllWallets(1L);

        assertEquals(wallets.size(), result.size());
        verify(walletRepository, times(1)).findAllWallets(anyLong());
    }

}
