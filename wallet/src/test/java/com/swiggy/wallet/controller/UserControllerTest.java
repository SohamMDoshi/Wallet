package com.swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.dto.TransferAmountRequestBody;
import com.swiggy.wallet.dto.UserRegistrationRequest;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.service.UserService;
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

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testRegisterUser() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest("testUser", "password", Country.USA);
        Users createdUser = new Users();
        createdUser.setId(1L);
        createdUser.setUsername(request.getUsername());
        createdUser.setPassword(request.getPassword());

        when(userService.registerUser(request.getUsername(), request.getPassword(),request.getCountry())).thenReturn(createdUser);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.username").value(createdUser.getUsername()))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(userService, times(1)).registerUser(request.getUsername(), request.getPassword(),request.getCountry());
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDeleteUser() throws Exception {
        Users user = new Users("testUser","password",Country.USA);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User got deleted successfully"));

        verify(userService, times(1)).deleteUser(user);
    }





}

