package com.swiggy.wallet.entity;

import com.swiggy.wallet.Expection.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    @Test
    public void testCreatingMoneyWithNegativeValue_ExpectException() {
        assertThrows(InvalidAmountException.class, () -> new Money(new BigDecimal("-5.0"),Currency.USD));
    }

    @Test
    public void testAddingTwoMoneyWithBaseCurrency() {
        Money moneyOne = new Money(new BigDecimal("5.00"),Currency.USD);
        Money moneyTwo = new Money(new BigDecimal("5.23"),Currency.USD);

        moneyOne.add(moneyTwo,moneyOne.getCurrency());
        Money expectedMoney = new Money(new BigDecimal("10.23"),Currency.USD);
        assertEquals(expectedMoney,moneyOne);
    }

    @Test
    public void testAddingMoneyWithDifferentCurrencyTOBaseCurrencyUSD() {
        Money moneyOne = new Money(new BigDecimal("5.0"),Currency.USD);
        Money moneyTwo = new Money(new BigDecimal("7.0"),Currency.EUR);

        moneyOne.add(moneyTwo,moneyOne.getCurrency());
        Money expectedMoney = new Money(new BigDecimal("12.61"),Currency.USD);
        assertEquals(expectedMoney,moneyOne);
    }

    @Test
    public void testAddingMoneyWithTwoDifferentCurrency_ExpectOutputInBaseCurrencyUSD() {
        Money moneyOne = new Money(new BigDecimal("5.0"),Currency.GBP);
        Money moneyTwo = new Money(new BigDecimal("7.0"),Currency.EUR);

        moneyOne.add(moneyTwo,moneyOne.getCurrency());
        Money expectedMoney = new Money(new BigDecimal("10.94"),Currency.GBP);
        assertEquals(expectedMoney,moneyOne);
    }
}
