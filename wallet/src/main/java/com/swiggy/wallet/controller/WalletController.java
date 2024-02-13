package com.swiggy.wallet.controller;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.dto.AmountDTO;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/")
    public ResponseEntity<Wallet> createWallet() {
        return new ResponseEntity<>(walletService.createWallet(),HttpStatus.CREATED);
    }

    @GetMapping("checkBalance/{id}")
    public double checkBalance(@PathVariable Long id) {
        return walletService.checkBalance(id);
    }
    @PutMapping("/deposit/{id}")
    public ResponseEntity<String> deposit(@PathVariable Long id, @RequestBody AmountDTO amountDTO) {
        walletService.deposit(id,amountDTO.getAmount());
        return new ResponseEntity<>("Amount Deposited", HttpStatus.OK);

    }
    @PutMapping("/withdraw/{id}")
    public ResponseEntity<String> withdraw(@PathVariable Long id, @RequestBody AmountDTO amountDTO) throws InsufficientBalanceException {
        walletService.withdraw(id,amountDTO.getAmount());
        return new ResponseEntity<>("Amount Withdrawal", HttpStatus.OK);
    }
}
