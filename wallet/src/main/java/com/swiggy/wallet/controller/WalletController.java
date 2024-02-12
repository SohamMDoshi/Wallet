package com.swiggy.wallet.controller;

import com.swiggy.wallet.dto.AmountDTO;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public Wallet createWallet() {
        return walletService.createWallet();
    }

    @GetMapping("/{id}/balance")
    public double checkBalance(@PathVariable Long id) {
        return walletService.checkBalance(id);
    }
    @PutMapping("/{id}/deposit")
    public void deposit(@PathVariable Long id, @RequestBody AmountDTO amountDTO) {
        walletService.deposit(id,amountDTO.getAmount());
    }
    @PutMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Long id, @RequestBody AmountDTO amountDTO){
        walletService.withdraw(id,amountDTO.getAmount());
    }
}
