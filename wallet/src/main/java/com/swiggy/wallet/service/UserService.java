package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.entity.*;


public interface UserService {

    Users registerUser(String username, String password, Country country);

    Wallet createNewWallet(Users user);

    String deleteUser(Users user) throws UserNotFoundException;

}
