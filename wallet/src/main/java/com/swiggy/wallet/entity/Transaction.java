package com.swiggy.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long currentUserId;
    private String otherUsername;
    @Embedded
    private Money amount;
    private LocalDateTime dateTime;

    public Transaction(TransactionType type,Long currentUserId, String otherUsername, Money amount, LocalDateTime dateTime) {
        this.type = type;
        this.currentUserId = currentUserId;
        this.otherUsername = otherUsername;
        this.amount = amount;
        this.dateTime = dateTime;
    }
}
