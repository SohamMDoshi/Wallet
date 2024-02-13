package com.swiggy.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.controller.WalletController;
import com.swiggy.wallet.dto.AmountDTO;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.WalletRepository;
import com.swiggy.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
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
        AmountDTO amountDTO = new AmountDTO(50.0);
        String requestBody = objectMapper.writeValueAsString(amountDTO);

        mockMvc.perform(put("/wallet/deposit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testWithdraw() throws Exception {
        mockMvc.perform(post("/wallet/")).andExpect(MockMvcResultMatchers.status().isCreated());
        AmountDTO amountDTO = new AmountDTO(50.0);
        String requestBody = objectMapper.writeValueAsString(amountDTO);

        mockMvc.perform(put("/wallet/deposit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        AmountDTO amountDTO1 = new AmountDTO(10.0);
        String requestBody1 = objectMapper.writeValueAsString(amountDTO1);

        mockMvc.perform(put("/wallet/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testWithdrawMoreThanBalance_ExpectBadRequest() throws Exception {
        mockMvc.perform(post("/wallet/")).andExpect(MockMvcResultMatchers.status().isCreated());
        AmountDTO amountDTO = new AmountDTO(50.0);
        String requestBody = objectMapper.writeValueAsString(amountDTO);

        mockMvc.perform(put("/wallet/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
