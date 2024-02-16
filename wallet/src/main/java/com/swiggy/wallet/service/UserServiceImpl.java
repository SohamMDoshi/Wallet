package com.swiggy.wallet.service;

import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    public Users registerUser(String username, String password) {
        // Create a new users
        Users users = new Users(username, password);
        userRepository.save(users);
        // Create a wallet for the users
        users.setWallet(walletService.createWallet(users.getId()));

        return users;
    }
}
