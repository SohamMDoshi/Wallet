package com.swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.WalletRepository;
import com.swiggy.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDeposit() throws Exception {
        Money depositAmount = new Money(new BigDecimal("50"), Currency.USD);
        String requestBody = objectMapper.writeValueAsString(depositAmount);

        Wallet expectedWalletResponse = new Wallet(1L, depositAmount);
        String responseBody = objectMapper.writeValueAsString(expectedWalletResponse);

        when(walletService.deposit(1L,depositAmount)).thenReturn(expectedWalletResponse);

        mockMvc.perform(put("/wallet/1/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        verify(walletService,times(1)).deposit(1L,depositAmount);
    }

    @Test
    public void testWithdraw() throws Exception {
        Wallet originalWallet = new Wallet(1L, new Money(new BigDecimal("100.0"), Currency.USD));
        when(walletRepository.findById(eq(1L))).thenReturn(Optional.of(originalWallet));

        Wallet updatedWallet = new Wallet(1L, new Money(new BigDecimal("95.0"), Currency.USD)); // Assuming $5 is withdrawn
        when(walletService.withdraw(eq(1L), any(Money.class))).thenReturn(updatedWallet);

        // Withdrawal amount
        Money withdrawalAmount = new Money(new BigDecimal("5.0"), Currency.USD);
        String requestBody = objectMapper.writeValueAsString(withdrawalAmount);

        // Perform API request and verify response
        mockMvc.perform(put("/wallet/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(updatedWallet)));

        verify(walletService,times(1)).withdraw(1L,withdrawalAmount);
    }

    @Test
    public void testWithdrawMoreThanBalance_ExpectInsufficientBalanceException() throws Exception {
        mockMvc.perform(post("/wallet/")).andExpect(status().isCreated());

        Money withdrawAmount = new Money(new BigDecimal("5"), Currency.EUR);
        String requestBody = objectMapper.writeValueAsString(withdrawAmount);

        when(walletService.withdraw(eq(1L), any(Money.class)))
                .thenThrow(new InsufficientBalanceException());

        mockMvc.perform(put("/wallet/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(InsufficientBalanceException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Insufficient Balance", result.getResolvedException().getMessage()));

        verify(walletService,times(1)).withdraw(eq(1L), any(Money.class));
    }


    @Test
    void testGetWallets() throws Exception {
        List<Wallet> mockWallets = Arrays.asList(
                new Wallet(1L, new Money(new BigDecimal("100.0"), Currency.USD)),
                new Wallet(2L, new Money(new BigDecimal("50.0"), Currency.USD))
        );
        when(walletService.getAllWallets()).thenReturn(mockWallets);

        String expectedJsonResponse = objectMapper.writeValueAsString(mockWallets);

        mockMvc.perform(get("/wallet/wallets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));

        verify(walletService, times(1)).getAllWallets();
    }

}
