package com.swiggy.wallet.dto;

import com.swiggy.wallet.entity.Money;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private String response;
    private Money balance;
}
