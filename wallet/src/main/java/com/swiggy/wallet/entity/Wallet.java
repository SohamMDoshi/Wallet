package com.swiggy.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.InvalidAmountException;
import com.swiggy.wallet.Expection.OperationNotPossible;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Money currentBalance;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    public Wallet(Users users) {
        this.users = users;
        this.currentBalance = new Money(new BigDecimal("0.00"),Currency.USD);
    }

    public void deposit(Money money)throws InvalidAmountException {
        try{
            currentBalance.add(money);
        }catch (InvalidAmountException e) {
            throw new InvalidAmountException();
        }
    }

    public void withdraw(Money money) throws InsufficientBalanceException, InvalidAmountException {
        try{
            currentBalance.minus(money);
        }catch (OperationNotPossible e) {
            throw new InsufficientBalanceException();
        }catch (InvalidAmountException e) {
            throw new InvalidAmountException();
        }
    }

}
