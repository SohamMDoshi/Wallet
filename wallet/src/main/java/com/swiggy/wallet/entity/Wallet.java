package com.swiggy.wallet.entity;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double currentBalance;


    public Wallet() {
        this.currentBalance = 0.0;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void deposit(double amount) {
        currentBalance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if(currentBalance < amount) throw new InsufficientBalanceException();
        currentBalance-=amount;
    }

}
