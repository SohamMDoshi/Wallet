package com.swiggy.wallet.dto;

public class AmountDTO {
    private final double amount;

    public AmountDTO(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
