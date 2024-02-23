package com.swiggy.wallet.dto;

import com.swiggy.wallet.entity.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferAmountRequestBody {

    @NotBlank(message = "receiver username is required")
    private String receiverUsername;

    @NotBlank(message = "receiver walletId is required")
    private Long receiverWalletId;

    @NotNull(message = "Amount value is required")
    private Money money;
}
