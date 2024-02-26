package com.swiggy.wallet.dto;

import com.swiggy.wallet.entity.Money;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TransactionAmountDTO {
    private Money senderAmount;
    private Money receiverAmount;
}
