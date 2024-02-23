package com.swiggy.wallet.controller;

import com.swiggy.wallet.Expection.IncorrectUserException;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.SelfTransferException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.dto.TransferAmountRequestBody;
import com.swiggy.wallet.entity.Transaction;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/transaction-management")
@Validated
public class TransactionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;


    @PutMapping("/users/{userId}/transfer-amount")
    public ResponseEntity<TransactionResponse> transferAmount(@Valid @PathVariable Long userId, @RequestBody TransferAmountRequestBody requestBody) throws InsufficientBalanceException, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        if(!Objects.equals(user.getId(), userId)) throw new IncorrectUserException(userId,username);
        if(username.equals(requestBody.getReceiverUsername())) throw new SelfTransferException();
        TransactionResponse response = transactionService.transferAmount(user, userId, requestBody.getReceiverUsername(),
                requestBody.getReceiverWalletId(), requestBody.getMoney());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/history")
    public ResponseEntity<List<Transaction>> transferHistory(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        if(!Objects.equals(user.getId(), userId)) throw new IncorrectUserException(userId,username);
        return new ResponseEntity<>(transactionService.transactionHistory(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/history/date-range")
    public ResponseEntity<List<Transaction>> getTransactionHistoriesInDateRange(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS") LocalDateTime endDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        if(!Objects.equals(user.getId(), userId)) throw new IncorrectUserException(userId,username);
        return new ResponseEntity<>(transactionService.getTransactionHistoriesInDateRange(userId,startDate, endDate), HttpStatus.OK);
    }
}
