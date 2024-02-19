package com.swiggy.wallet.dto;

import com.swiggy.wallet.entity.Money;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferAmountRequestBody {

    private String receiverUsername;
    private Money money;
}
