package com.swiggy.wallet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class WalletTest {


    @Test
    public void testDepositingMoneyInWallet() {
        Money currentBalance = spy(new Money(BigDecimal.ZERO,Currency.USD));
        Users user = new Users("testUser","Pass",Country.USA);
        Wallet wallet = spy(new Wallet(1L,currentBalance,user));
        Money depositMoney = new Money(BigDecimal.TWO,Currency.USD);
        wallet.deposit(depositMoney);

        verify(currentBalance,times(1)).add(depositMoney);
        assertEquals(wallet.getCurrentBalance().toString(), new Money(new BigDecimal("2.00"),Currency.USD).toString());
    }

    @Test
    public void testWithDrawMoneyInWallet() {
        Money currentBalance = spy(new Money(BigDecimal.TEN,Currency.USD));
        Users user = new Users("testUser","Pass",Country.USA);
        Wallet wallet = spy(new Wallet(1L,currentBalance,user));
        Money withdrawMoney = new Money(BigDecimal.TWO,Currency.USD);
        wallet.withdraw(withdrawMoney);

        verify(currentBalance,times(1)).minus(withdrawMoney);
        assertEquals(wallet.getCurrentBalance().toString(), new Money(new BigDecimal("8.00"),Currency.USD).toString());
    }
}
