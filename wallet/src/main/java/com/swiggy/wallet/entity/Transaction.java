package com.swiggy.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @JsonIgnore
    private Long userId;
    private String otherParty;
    @Embedded
    private Money amount;
    private LocalDateTime dateTime;

    private Double serviceFee;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double getServiceFee() {
        return serviceFee;
    }


    public Transaction(TransactionType type,Long userId, String otherParty, Money amount, LocalDateTime dateTime,Double serviceFee) {
        this.type = type;
        this.userId = userId;
        this.otherParty = otherParty;
        this.amount = amount;
        this.dateTime = dateTime;
        this.serviceFee = serviceFee;
    }
}
