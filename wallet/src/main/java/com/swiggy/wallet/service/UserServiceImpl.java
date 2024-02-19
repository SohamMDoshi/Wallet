package com.swiggy.wallet.service;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserAlreadyExistsException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.entity.Money;
import com.swiggy.wallet.entity.Users;
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

    public Users registerUser(String username, String password)  throws UserAlreadyExistsException{
        if(userRepository.findByUsername(username) != null) throw new UserAlreadyExistsException("User is already exits with username "+username);
        Users users = new Users(username, password);
        users.setPassword(passwordEncoder().encode(users.getPassword()));
        userRepository.save(users);
        users.setWallet(walletService.createWallet(users.getId()));
        return userRepository.save(users);
    }

    @Override
    public TransactionResponse transferAmount(Users sender, String receiverUsername, Money transferAmount) throws InsufficientBalanceException, UserNotFoundException {
        Users receiver = userRepository.findByUsername(receiverUsername);
        if(receiver == null) throw new UserNotFoundException("User not found with username " + receiverUsername);
        try{
           return walletService.transferMoney(sender, receiver, transferAmount);
        }catch (InsufficientBalanceException e) {throw  new InsufficientBalanceException();}
    }

    @Override
    public String deleteUser(Users users) throws UserNotFoundException {
        if(userRepository.findByUsername(users.getUsername()) == null)
            throw new UserNotFoundException("User not found with username "+users.getUsername());
        userRepository.delete(users);
        return "User got deleted successfully";
    }
}
