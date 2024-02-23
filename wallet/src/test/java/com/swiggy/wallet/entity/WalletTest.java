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
        Wallet wallet = spy(new Wallet(user));
        Money depositMoney = new Money(new BigDecimal("5.00"),Currency.USD);
        wallet.deposit(depositMoney);
        wallet.deposit(depositMoney);

        //verify(currentBalance,times(1)).add(depositMoney,wallet.getCurrentBalance().getCurrency());
        assertEquals(wallet.getCurrentBalance().getAmount(),depositMoney.getAmount().multiply(BigDecimal.TWO));
    }

//    @Test
//    public void testWithDrawMoneyInWallet() {
//        Money withdrawMoney = new Money(new BigDecimal("5.00"),Currency.USD);
//        wallet.withdraw(withdrawMoney);
//        verify(money,times(1)).minus(withdrawMoney,wallet.getCurrentBalance().getCurrency());
//    }
}
