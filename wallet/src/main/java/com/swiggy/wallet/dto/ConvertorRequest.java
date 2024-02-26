package com.swiggy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConvertorRequest {
    private String baseCurrency;
    private String targetCurrency;
    private double amount;
}
