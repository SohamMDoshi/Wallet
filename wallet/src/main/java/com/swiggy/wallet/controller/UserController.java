package com.swiggy.wallet.controller;

import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.dto.TransferAmountRequestBody;
import com.swiggy.wallet.dto.UserRegistrationRequest;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/")
    public ResponseEntity<Users> registerUser(@RequestBody UserRegistrationRequest request) {
        Users users = userService.registerUser(request.getUsername(), request.getPassword());
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteUser() throws UserNotFoundException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        userService.deleteUser(user);
        return new ResponseEntity<>("User got deleted successfully",HttpStatus.ACCEPTED);
    }

    @PutMapping("/transfer")
    public TransactionResponse transferAmount(@RequestBody TransferAmountRequestBody requestBody) throws InsufficientBalanceException, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        return userService.transferAmount(user, requestBody.getReceiverUsername(), requestBody.getMoney());
    }
}
