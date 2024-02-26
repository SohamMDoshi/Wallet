package com.swiggy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionListDTO {
    private Transaction transaction;
    private Money serviceFee;

    // Constructor with null check for service fee
    public TransactionListDTO(Transaction transaction) {
        this.transaction = transaction;
        this.serviceFee = transaction.getServiceFee() != null ? new Money(BigDecimal.valueOf(transaction.getServiceFee()), transaction.getTransferAmount().getCurrency()) : null;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Money getServiceFee() {
        return serviceFee;
    }
}
