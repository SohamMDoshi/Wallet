package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.TransactionHistory;
import com.swiggy.wallet.entity.Users;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    public Users registerUser(String username, String password);

    TransactionResponse transferAmount(Users users,String receiverUsername, Money amount);

    List<TransactionHistory> transactionHistory(Users users);

    public String deleteUser(Users user) throws UserNotFoundException;

    public List<TransactionHistory> getTransactionHistoriesInDateRange(Users users,LocalDateTime startDate, LocalDateTime endDate);
}
