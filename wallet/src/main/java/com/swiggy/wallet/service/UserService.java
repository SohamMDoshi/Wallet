package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    public Users registerUser(String username, String password, Country country);

    public Wallet createNewWallet(Users user);

    public String deleteUser(Users user) throws UserNotFoundException;

}
