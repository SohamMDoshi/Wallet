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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsersServiceTest {
    @Mock
    private WalletService walletService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

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


    @Test
    void testGetTransactionHistoriesInDateRange() {
        Users users = Mockito.mock(Users.class);

        List<TransactionHistory> transactionHistories = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 1, 31, 23, 59);

        transactionHistories.add(new TransactionHistory(TransactionType.SENT, "otherUser1", new Money(new BigDecimal("100.00"),Currency.USD),
                LocalDateTime.of(2022, 1, 5, 12, 0)));
        transactionHistories.add(new TransactionHistory(TransactionType.RECEIVED, "otherUser2", new Money(new BigDecimal("50.00"),Currency.USD),
                LocalDateTime.of(2022, 1, 15, 10, 30)));
        transactionHistories.add(new TransactionHistory(TransactionType.SENT, "otherUser3", new Money(new BigDecimal("200.00"),Currency.USD),
                LocalDateTime.of(2022, 1, 25, 8, 15)));
        transactionHistories.add(new TransactionHistory(TransactionType.RECEIVED, "otherUser4", new Money(new BigDecimal("150.00"),Currency.USD),
                LocalDateTime.of(2021, 12, 20, 14, 45)));
        transactionHistories.add(new TransactionHistory(TransactionType.SENT, "otherUser5", new Money(new BigDecimal("75.00"),Currency.USD),
                LocalDateTime.of(2022, 2, 10, 16, 20)));

        List<TransactionHistory> expectedTransactionHistories = new ArrayList<>(transactionHistories.subList(0, 3));

        when(transactionRepository.findTransactionsByUserIdAndTimestamp(users.getId(),startDate,endDate)).thenReturn(expectedTransactionHistories);

        List<TransactionHistory> result = userService.getTransactionHistoriesInDateRange(users, startDate, endDate);

        assertEquals(3, result.size());
    }
}

