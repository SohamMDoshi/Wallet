package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.entity.*;


public interface UserService {

    public Users registerUser(String username, String password, Country country);

    public Wallet createNewWallet(Users user);

    public String deleteUser(Users user) throws UserNotFoundException;

}
