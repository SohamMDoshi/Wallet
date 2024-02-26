package com.swiggy.wallet.controller;

import com.swiggy.wallet.Expection.IncorrectWalletException;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import com.swiggy.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/wallets")
@Validated
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;


    @PutMapping("/{walletId}/deposit")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable Long walletId,@Valid @RequestBody Money money) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        if(walletRepository.findByUserId(walletId, user.getId()).isEmpty()) throw new IncorrectWalletException(walletId,username);
        Wallet wallet = walletService.deposit(walletId,money);
        TransactionResponse transactionResponse = new TransactionResponse("Amount Deposited Successfully",wallet.getCurrentBalance());
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);

    }
    @PutMapping("/{walletId}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable Long walletId,@Valid @RequestBody Money money) throws InsufficientBalanceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        if(walletRepository.findByUserId(walletId, user.getId()).isEmpty()) throw new IncorrectWalletException(walletId,username);
        Wallet wallet = walletService.withdraw(walletId, money);
        TransactionResponse transactionResponse = new TransactionResponse("Amount withdraw Successfully",wallet.getCurrentBalance());
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Set<Wallet>> getWallets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        return new ResponseEntity<>(walletService.getAllWallets(user.getId()), HttpStatus.OK);
    }


}
