package com.swiggy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AmountDTO {
    private double amount;

    public AmountDTO() {}

    public AmountDTO(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
