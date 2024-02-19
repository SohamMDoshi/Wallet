package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
import org.springframework.security.core.userdetails.User;

public interface UserService {

    public Users registerUser(String username, String password);

    TransactionResponse transferAmount(Users users,String receiverUsername, Money amount);

    public String deleteUser(Users user) throws UserNotFoundException;
}
