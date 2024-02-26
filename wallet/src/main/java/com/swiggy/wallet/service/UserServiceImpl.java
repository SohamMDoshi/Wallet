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
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    private static PasswordEncoder passwordEncoder;

    @Override
    public Users registerUser(String username, String password, Country country) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with username " + username);
        }
        String encodedPassword = passwordEncoder.encode(password); // Encode the password
        Users user = new Users(username, encodedPassword, country);
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
    @Transactional
    public String deleteUser(Users users) throws UserNotFoundException {
        userRepository.deleteById(users.getId());
        return "User got deleted successfully";
    }

}
