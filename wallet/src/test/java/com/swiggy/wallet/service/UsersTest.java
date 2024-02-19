package com.swiggy.wallet.service;

import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Currency;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsersTest {
    @Mock
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

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
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(walletService.createWallet(mockUser.getId())).thenReturn(new Wallet());

        Users registeredUser = userService.registerUser("testUser", "password");

        assertEquals("testUser", registeredUser.getUsername());
        assertNotNull(registeredUser.getWallet());
    }


    @Test
    public void testTransferAmountSuccess() throws Exception {
        Users sender = new Users("sender", "password");
        Users receiver = new Users("receiver", "password");

        Money transferAmount = new Money(new BigDecimal("50"), Currency.USD);

        when(userRepository.findByUsername("receiver")).thenReturn(receiver);
        when(walletService.transferMoney(sender,receiver,transferAmount))
                .thenReturn(new TransactionResponse("Transferred amount successful",new BigDecimal("50.00")));

        TransactionResponse response = userService.transferAmount(sender, "receiver", new Money(new BigDecimal("50.00"),Currency.USD));

        assertEquals("Transferred amount successful", response.getResponse());
        assertEquals(new BigDecimal("50.00"), response.getBalance());

        verify(userRepository,times(1)).findByUsername("receiver");
        verify(walletService,times(1)).transferMoney(sender, receiver, new Money(new BigDecimal("50.00"),Currency.USD));
    }


    @Test
    public void testDeleteUser() throws Exception {
        Users user = new Users("testUser", "password");
        userService.deleteUser(user);
        verify(userRepository,times(1)).delete(user);
    }


}

