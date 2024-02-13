package com.swiggy.wallet.entity;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.InvalidAmountException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
public class Money {

    private BigDecimal amount;
    private Currency currency;

    public Money (BigDecimal amount, Currency currency) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException();
        BigDecimal conversionRate = currency.getConversionRateTOUSD();
        this.amount = amount.multiply(conversionRate).setScale(2, RoundingMode.HALF_UP);;
        this.currency = Currency.USD;
    }

    public void add(Money money) {
        if(money.getAmount().compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException();
        if (!currency.equals(money.getCurrency())) {
            BigDecimal conversionRate = money.getCurrency().getConversionRateTOUSD();
            BigDecimal amountInUSD = money.getAmount().multiply(conversionRate);
            amount = amount.add(amountInUSD).setScale(2, RoundingMode.HALF_UP);
        }
        else amount = amount.add(money.getAmount()).setScale(2, RoundingMode.HALF_UP);;
    }

    public void minus(Money money) {
        if(money.getAmount().compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException();
        if (!currency.equals(money.getCurrency())) {
            BigDecimal conversionRate = money.getCurrency().getConversionRateTOUSD();
            BigDecimal amountInUSD = money.getAmount().multiply(conversionRate);
            if(amount.compareTo(amountInUSD) < 0) throw new InsufficientBalanceException();
            else amount = amount.subtract(amountInUSD).setScale(2, RoundingMode.HALF_UP);;
        }
        else {
            if(amount.compareTo(money.getAmount()) < 0) throw new InsufficientBalanceException();
            else amount = amount.subtract(money.getAmount()).setScale(2, RoundingMode.HALF_UP);;
        }
    }
}
