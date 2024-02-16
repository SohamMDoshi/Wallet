package com.swiggy.wallet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class WalletTest {


    @Mock
    private Money money;

    @InjectMocks
    private Wallet wallet;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    public void testDepositingMoneyInWallet() {
        Money depositMoney = new Money(new BigDecimal("5.00"),Currency.USD);
        wallet.deposit(depositMoney);
        verify(money,times(1)).add(depositMoney);
    }

    @Test
    public void testWithDrawMoneyInWallet() {
        Money withdrawMoney = new Money(new BigDecimal("5.00"),Currency.USD);
        wallet.withdraw(withdrawMoney);
        verify(money,times(1)).minus(withdrawMoney);
    }
}
