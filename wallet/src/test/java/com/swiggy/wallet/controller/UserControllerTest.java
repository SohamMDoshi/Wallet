package com.swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.dto.TransferAmountRequestBody;
import com.swiggy.wallet.dto.UserRegistrationRequest;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import com.swiggy.wallet.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testRegisterUser() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest("testUser", "password");
        Users createdUser = new Users();
        createdUser.setId(1L);
        createdUser.setUsername(request.getUsername());
        createdUser.setPassword(request.getPassword());

        when(userService.registerUser(request.getUsername(), request.getPassword())).thenReturn(createdUser);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.username").value(createdUser.getUsername()))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(userService, times(1)).registerUser(request.getUsername(), request.getPassword());
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDeleteUser() throws Exception {
        Users user = new Users("testUser","password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        mockMvc.perform(delete("/users/"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User got deleted successfully"));

        verify(userService, times(1)).deleteUser(user);
    }


    @Test
    @WithMockUser(username = "testUser")
    void testTransferAmount() throws Exception {
        Users user = new Users("testUser","password");
        user.setId(1L);
        TransferAmountRequestBody requestBody = new TransferAmountRequestBody("receiverUser", new Money(BigDecimal.TEN, Currency.USD));
        TransactionResponse expectedResponse = new TransactionResponse("Transferred amount successful", BigDecimal.TEN);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        when(userService.transferAmount(user, requestBody.getReceiverUsername(), requestBody.getMoney())).thenReturn(expectedResponse);

        mockMvc.perform(put("/users/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(expectedResponse.getResponse()))
                .andExpect(jsonPath("$.balance").value(expectedResponse.getBalance()));

        verify(userService, times(1)).transferAmount(user, requestBody.getReceiverUsername(), requestBody.getMoney());
    }

    @Test
    @WithMockUser(username = "testUser")
    void testTransferAmountWhenInsufficientBalance_ExpectedException() throws Exception {
        Users user = new Users("testUser","password");
        user.setId(1L);
        TransferAmountRequestBody requestBody = new TransferAmountRequestBody("receiverUser", new Money(BigDecimal.TEN, Currency.USD));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        when(userService.transferAmount(user, requestBody.getReceiverUsername(), requestBody.getMoney())).thenThrow(new InsufficientBalanceException());

        mockMvc.perform(put("/users/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(InsufficientBalanceException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Insufficient Balance", result.getResolvedException().getMessage()));

        verify(userService, times(1)).transferAmount(user, requestBody.getReceiverUsername(), requestBody.getMoney());
    }


}

