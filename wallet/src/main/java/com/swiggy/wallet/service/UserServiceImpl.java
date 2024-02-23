package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.UserAlreadyExistsException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.entity.Country;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Users registerUser(String username, String password, Country country)  throws UserAlreadyExistsException{
        if(userRepository.findByUsername(username).isPresent()) throw new UserAlreadyExistsException("User is already exits with username "+username);
        Users user = new Users(username, password,country);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        Users updatedUser = userRepository.save(user);
        Wallet wallet = walletService.createWallet(updatedUser.getId());
        updatedUser.getWallets().add(wallet);
        return updatedUser;
    }

    @Override
    public Wallet createNewWallet(Users user) {
        Wallet wallet = walletService.createWallet(user.getId());
        user.getWallets().add(wallet);
        return wallet;
    }

    @Override
    public String deleteUser(Users users) throws UserNotFoundException {
        userRepository.delete(users);
        return "User got deleted successfully";
    }

}
