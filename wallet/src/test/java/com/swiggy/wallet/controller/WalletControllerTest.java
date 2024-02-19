package com.swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import com.swiggy.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testDeposit() throws Exception {
        Users user = new Users();
        user.setId(1L);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Money depositAmount = new Money(new BigDecimal("50"), Currency.USD);
        String requestBody = objectMapper.writeValueAsString(depositAmount);

        Wallet expectedWalletResponse = new Wallet(user);
        expectedWalletResponse.setId(1L);
        expectedWalletResponse.setCurrentBalance(depositAmount);
        expectedWalletResponse.setUsers(user);
        TransactionResponse expectedResponse = new TransactionResponse("Amount Deposited Successfully", new BigDecimal("50.00"));
        String responseBody = objectMapper.writeValueAsString(expectedResponse);

        when(walletService.deposit(eq(1L), any(Money.class))).thenReturn(expectedWalletResponse);

        mockMvc.perform(put("/wallets/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        verify(walletService, times(1)).deposit(anyLong(), any(Money.class));
    }


    @Test
    public void testWithdraw() throws Exception {
        Users user = new Users();
        user.setId(1L);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Money withdrawAmount = new Money(new BigDecimal("50"), Currency.USD);
        String requestBody = objectMapper.writeValueAsString(withdrawAmount);

        Wallet expectedWalletResponse = new Wallet(user);
        expectedWalletResponse.setId(1L);
        expectedWalletResponse.setCurrentBalance(withdrawAmount);
        expectedWalletResponse.setUsers(user);
        TransactionResponse expectedResponse = new TransactionResponse("Amount withdraw Successfully", new BigDecimal("50.00"));
        String responseBody = objectMapper.writeValueAsString(expectedResponse);

        when(walletService.withdraw(eq(1L), any(Money.class))).thenReturn(expectedWalletResponse);

        mockMvc.perform(put("/wallets/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        verify(walletService, times(1)).withdraw(anyLong(), any(Money.class));
    }

    @Test
    public void testWithdrawMoreThanBalance_ExpectInsufficientBalanceException() throws Exception {
        Users user = new Users();
        user.setId(1L);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Money withdrawAmount = new Money(new BigDecimal("5"), Currency.EUR);
        String requestBody = objectMapper.writeValueAsString(withdrawAmount);

        when(walletService.withdraw(eq(1L), any(Money.class)))
                .thenThrow(new InsufficientBalanceException());

        mockMvc.perform(put("/wallets/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(InsufficientBalanceException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Insufficient Balance", result.getResolvedException().getMessage()));

        verify(walletService,times(1)).withdraw(eq(1L), any(Money.class));
    }


    @Test
    void testGetAllWallets() throws Exception {
        Users user = new Users();
        user.setId(1L);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<Wallet> mockWallets = Arrays.asList(
                new Wallet(1L, new Money(new BigDecimal("100.0"), Currency.USD),user),
                new Wallet(2L, new Money(new BigDecimal("50.0"), Currency.USD),user)
        );
        when(walletService.getAllWallets()).thenReturn(mockWallets);

        String expectedJsonResponse = objectMapper.writeValueAsString(mockWallets);

        mockMvc.perform(get("/wallets/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));

        verify(walletService, times(1)).getAllWallets();
    }

}
