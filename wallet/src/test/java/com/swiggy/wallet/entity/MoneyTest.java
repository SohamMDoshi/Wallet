package com.swiggy.wallet.entity;

import com.swiggy.wallet.Expection.CurrencyMismatchException;
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
    public void testAddingTwoMoneyWithSameCurrency() {
        Money moneyOne = new Money(new BigDecimal("5.00"),Currency.USD);
        Money moneyTwo = new Money(new BigDecimal("5.23"),Currency.USD);

        moneyOne.add(moneyTwo);
        Money expectedMoney = new Money(new BigDecimal("10.23"),Currency.USD);
        assertEquals(expectedMoney,moneyOne);
    }

    @Test
    public void testAddingMoneyWithTwoDifferentCurrency_ExpectException() {
        Money moneyOne = new Money(new BigDecimal("5.0"),Currency.GBP);
        Money moneyTwo = new Money(new BigDecimal("7.0"),Currency.EUR);

        assertThrows(CurrencyMismatchException.class,()-> moneyOne.add(moneyTwo));
    }

    @Test
    public void testSubtractingTwoMoneyWithSameCurrency() {
        Money moneyOne = new Money(new BigDecimal("15.50"),Currency.USD);
        Money moneyTwo = new Money(new BigDecimal("5.25"),Currency.USD);

        moneyOne.minus(moneyTwo);
        Money expectedMoney = new Money(new BigDecimal("10.25"),Currency.USD);
        assertEquals(expectedMoney,moneyOne);
    }

    @Test
    public void testSubtractingMoneyWithTwoDifferentCurrency_ExpectException() {
        Money moneyOne = new Money(new BigDecimal("5.0"),Currency.GBP);
        Money moneyTwo = new Money(new BigDecimal("7.0"),Currency.EUR);

        assertThrows(CurrencyMismatchException.class,()-> moneyOne.minus(moneyTwo));
    }
}
