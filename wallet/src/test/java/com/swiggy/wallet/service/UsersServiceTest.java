package com.swiggy.wallet.service;

import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.*;
import com.swiggy.wallet.repository.TransactionRepository;
import com.swiggy.wallet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsersServiceTest {
    @Mock
    private WalletService walletService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() throws Exception {
        Users mockUser = new Users("testUser", "password");
        //when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.empty());
        mockUser.setId(1L);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(walletService.createWallet(mockUser.getId())).thenReturn(mock(Wallet.class));

        Users registeredUser = userService.registerUser("testUser", "password");

        //verify(userRepository,times(2)).save(mockUser);
        assertEquals("testUser", registeredUser.getUsername());
        assertFalse(registeredUser.getWallets().isEmpty());
    }



    @Test
    public void testDeleteUser() throws Exception {
        Users user = new Users("testUser", "password");
        userService.deleteUser(user);
        verify(userRepository,times(1)).delete(user);
    }



}

