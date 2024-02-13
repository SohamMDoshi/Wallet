package com.swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.entity.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDeposit() throws Exception {
        mockMvc.perform(post("/wallet/")).andExpect(MockMvcResultMatchers.status().isCreated());
        Money depositAmount = new Money(new BigDecimal("50"), Currency.USD);
        String requestBody = objectMapper.writeValueAsString(depositAmount);

        mockMvc.perform(put("/wallet/deposit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testWithdraw() throws Exception {
        mockMvc.perform(post("/wallet/")).andExpect(MockMvcResultMatchers.status().isCreated());
        Money depositAmount = new Money(new BigDecimal("50"), Currency.USD);
        String requestBody = objectMapper.writeValueAsString(depositAmount);

        mockMvc.perform(put("/wallet/deposit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Money withdrawAmount = new Money(new BigDecimal("5"), Currency.EUR);
        String requestBody1 = objectMapper.writeValueAsString(withdrawAmount);

        mockMvc.perform(put("/wallet/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testWithdrawMoreThanBalance_ExpectBadRequest() throws Exception {
        mockMvc.perform(post("/wallet/")).andExpect(MockMvcResultMatchers.status().isCreated());
        Money withdrawAmount = new Money(new BigDecimal("5"), Currency.EUR);
        String requestBody = objectMapper.writeValueAsString(withdrawAmount);

        mockMvc.perform(put("/wallet/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
