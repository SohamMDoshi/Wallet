package com.swiggy.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.InvalidAmountException;
import com.swiggy.wallet.Expection.OperationNotPossible;
import com.swiggy.wallet.customValidation.ValidCurrencyValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    public Wallet(Users users) {
        this.users = users;
        this.currentBalance = new Money(new BigDecimal("0.00"),users.getCountry().getCurrency());
    }

    public void deposit(Money money)throws InvalidAmountException {
        try{
            this.currentBalance.add(money,currentBalance.getCurrency());
        }catch (InvalidAmountException e) {
            throw new InvalidAmountException();
        }
    }

    public void withdraw(Money money) throws InsufficientBalanceException, InvalidAmountException {
        try{
            this.currentBalance.minus(money,currentBalance.getCurrency());
        }catch (OperationNotPossible e) {
            throw new InsufficientBalanceException();
        }catch (InvalidAmountException e) {
            throw new InvalidAmountException();
        }
    }

}
