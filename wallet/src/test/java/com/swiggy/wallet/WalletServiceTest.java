package com.swiggy.wallet;

import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.WalletRepository;
import com.swiggy.wallet.service.WalletService;
import com.swiggy.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;
    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    public void testCreatingWallet() {
        Wallet wallet = new Wallet();
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        double balance = walletService.checkBalance(1L);
        assertEquals(0.0, balance);
    }



}
