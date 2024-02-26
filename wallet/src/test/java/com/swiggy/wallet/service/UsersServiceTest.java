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
        Users mockUser = new Users("testUser", "password",Country.USA);
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(walletService.createWallet(anyLong())).thenReturn(any(Wallet.class));

        Users registeredUser = userService.registerUser("testUser", "password",Country.USA);

        assertEquals("testUser", registeredUser.getUsername());
        assertFalse(registeredUser.getWallets().isEmpty());
        verify(userRepository,times(1)).save(any(Users.class));
    }



    @Test
    public void testDeleteUser() throws Exception {
        Users user = new Users("testUser", "password",Country.USA);
        user.setId(1L);
        String res= userService.deleteUser(user);

        assertEquals("User got deleted successfully",res);
        verify(userRepository,times(1)).deleteById(anyLong());
    }



}

