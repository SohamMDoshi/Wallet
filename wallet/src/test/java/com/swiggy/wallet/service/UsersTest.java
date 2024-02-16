package com.swiggy.wallet.service;

import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsersTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WalletService walletService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterUser() {
        // Mock wallet creation
        Wallet wallet = new Wallet();
        when(walletService.createWallet(anyLong())).thenReturn(wallet);

        Users users = new Users("testUser", "password");
        when(userRepository.save(any(Users.class))).thenReturn(users);

        Users result = userService.registerUser("testUser", "password");

        verify(userRepository).save(argThat(user -> user.getUsername().equals("testUser") && user.getPassword().equals("password")));

        verify(walletService).createWallet(result.getId());

        assertEquals("testUser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals(wallet, result.getWallet());
    }
}

