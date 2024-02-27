package com.swiggy.wallet.entity;

import com.swiggy.wallet.Expection.CurrencyMismatchException;
import com.swiggy.wallet.Expection.InvalidAmountException;
import com.swiggy.wallet.Expection.OperationNotPossible;
import com.swiggy.wallet.customValidation.ValidCurrencyValue;
import com.swiggy.wallet.grpcClient.CurrencyConversionClient;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
public class Money {

    @NotNull(message = "amount is required")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)

    @ValidCurrencyValue
    @NotNull(message = "currency is required")
    private Currency currency;


    public Money (BigDecimal amount, Currency currency) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException();
        this.amount = amount;
        this.currency = currency;
    }

    public void add(Money money) {
        if (money.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }
        if(!this.currency.equals(money.getCurrency())) throw new CurrencyMismatchException(this.currency.toString());
        this.amount = this.amount.add(money.getAmount()).setScale(2, RoundingMode.HALF_UP);
    }

    public void minus(Money money) {
        if (money.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }
        if(!this.currency.equals(money.getCurrency()))
            throw new CurrencyMismatchException(this.currency.toString());
        if (this.amount.compareTo(money.getAmount()) < 0) {
            throw new OperationNotPossible();
        } else {
            this.amount = this.amount.subtract(money.getAmount()).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
