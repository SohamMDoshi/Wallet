package com.swiggy.wallet.Expection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorDetails {

    private LocalDateTime dateTime;
    private String message;
    private String details;
}
