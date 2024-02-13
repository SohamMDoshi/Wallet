package com.swiggy.wallet.entity;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Money currentBalance;

    public Wallet() {
        this.currentBalance = new Money(new BigDecimal("0.00"),Currency.USD);
    }

    public void deposit(Money money) {
        currentBalance.add(money);
    }

    public void withdraw(Money money) throws InsufficientBalanceException {
        currentBalance.minus(money);
    }

}
