package com.swiggy.wallet.entity;

import com.swiggy.wallet.Expection.InvalidAmountException;
import com.swiggy.wallet.Expection.OperationNotPossible;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
public class Money {

    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.USD;

    public Money (BigDecimal amount, Currency currency) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException();
        BigDecimal conversionRate = currency.getConversionRateTOUSD();
        this.amount = amount.multiply(conversionRate).setScale(2, RoundingMode.HALF_UP);
    }

    public void add(Money money) {
        if(money.getAmount().compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException();
        if (!currency.equals(money.getCurrency())) {
            BigDecimal conversionRate = money.getCurrency().getConversionRateTOUSD();
            BigDecimal amountInUSD = money.getAmount().multiply(conversionRate);
            amount = amount.add(amountInUSD).setScale(2, RoundingMode.HALF_UP);
        }
        else amount = amount.add(money.getAmount()).setScale(2, RoundingMode.HALF_UP);
    }

    public void minus(Money money) {
        if(money.getAmount().compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException();
        if (!currency.equals(money.getCurrency())) {
            BigDecimal conversionRate = money.getCurrency().getConversionRateTOUSD();
            BigDecimal amountInUSD = money.getAmount().multiply(conversionRate);
            if(amount.compareTo(amountInUSD) < 0) throw new OperationNotPossible();
            else amount = amount.subtract(amountInUSD).setScale(2, RoundingMode.HALF_UP);
        }
        else {
            if(amount.compareTo(money.getAmount()) < 0) throw new OperationNotPossible();
            else amount = amount.subtract(money.getAmount()).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
