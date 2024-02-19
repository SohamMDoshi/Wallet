package com.swiggy.wallet.controller;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.service.UserService;
import com.swiggy.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;


    @PutMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody Money money) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        Wallet wallet = walletService.deposit(user.getId(),money);
        TransactionResponse transactionResponse = new TransactionResponse("Amount Deposited Successfully",wallet.getCurrentBalance().getAmount());
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);

    }
    @PutMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody Money money) throws InsufficientBalanceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        Wallet wallet = walletService.withdraw(user.getId(), money);
        TransactionResponse transactionResponse = new TransactionResponse("Amount withdraw Successfully",wallet.getCurrentBalance().getAmount());
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Wallet>> getWallets() {
        return new ResponseEntity<>(walletService.getAllWallets(), HttpStatus.OK);
    }
}
