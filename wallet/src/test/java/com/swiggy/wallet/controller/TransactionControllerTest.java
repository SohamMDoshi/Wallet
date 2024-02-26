package com.swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.dto.TransferAmountRequestBody;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.service.TransactionService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TransactionService transactionService;



    @Test
    @WithMockUser(username = "testUser")
    void testTransferAmount() throws Exception {
        Users user = new Users("testUser","password",Country.USA);
        user.setId(1L);
        TransferAmountRequestBody requestBody = new TransferAmountRequestBody("receiverUser",2L,
                new Money(BigDecimal.TEN, Currency.USD));
        TransactionResponse expectedResponse = new TransactionResponse("Transferred amount successful", new Money(BigDecimal.TEN, Currency.USD));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        when(transactionService.transferAmount(user, user.getId(),requestBody.getReceiverUsername(),requestBody.getReceiverWalletId(),
                requestBody.getMoney())).thenReturn(expectedResponse);

        mockMvc.perform(put("/users/transfer-amount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(expectedResponse.getResponse()))
                .andExpect(jsonPath("$.balance").value(expectedResponse.getBalance()));

        verify(transactionService, times(1)).transferAmount(user, user.getId(),
                requestBody.getReceiverUsername(),requestBody.getReceiverWalletId(), requestBody.getMoney());
    }

    @Test
    @WithMockUser(username = "testUser")
    void testTransferAmountWhenInsufficientBalance_ExpectedException() throws Exception {
        Users user = new Users("testUser","password",Country.USA);
        user.setId(1L);
        TransferAmountRequestBody requestBody = new TransferAmountRequestBody("receiverUser",2L,
                new Money(BigDecimal.TEN, Currency.USD));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        when(transactionService.transferAmount(user, user.getId(),requestBody.getReceiverUsername(),requestBody.getReceiverWalletId(),
                requestBody.getMoney())).thenThrow(new InsufficientBalanceException());

        mockMvc.perform(put("/users/transfer-amount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(InsufficientBalanceException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Insufficient Balance", result.getResolvedException().getMessage()));

        verify(transactionService, times(1)).transferAmount(user, user.getId(),
                requestBody.getReceiverUsername(),requestBody.getReceiverWalletId(), requestBody.getMoney());    }

    @Test
    @WithMockUser(username = "testUser")
    void testGettingTransactionHistory() throws Exception {
        Users user = new Users("testUser","password", Country.USA);
        user.setId(1L);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        List<Transaction> response = Arrays.asList(
                mock(Transaction.class),
                mock(Transaction.class)
        );

        when(transactionService.transactionHistory(user.getId())).thenReturn(response);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        mockMvc.perform(get("/users/transfer-history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));

        verify(transactionService, times(1)).transactionHistory(user.getId());
    }

}
