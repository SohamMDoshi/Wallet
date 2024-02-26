package com.swiggy.wallet.controller;

import com.swiggy.wallet.Expection.IncorrectUserException;
import com.swiggy.wallet.Expection.InsufficientBalanceException;
import com.swiggy.wallet.Expection.SelfTransferException;
import com.swiggy.wallet.Expection.UserNotFoundException;
import com.swiggy.wallet.dto.TransactionResponse;
import com.swiggy.wallet.dto.TransferAmountRequestBody;
import com.swiggy.wallet.dto.UserRegistrationRequest;
import com.swiggy.wallet.entity.Transaction;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.entity.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        Users users = userService.registerUser(request.getUsername(), request.getPassword(),request.getCountry());
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/create-wallet")
    public ResponseEntity<Wallet> createNewWallet(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        if(!Objects.equals(user.getId(), userId)) throw new IncorrectUserException(userId,username);
        return new ResponseEntity<>(userService.createNewWallet(user),HttpStatus.CREATED);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) throws UserNotFoundException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        if(!Objects.equals(user.getId(), userId)) throw new IncorrectUserException(userId,username);
        return new ResponseEntity<>(userService.deleteUser(user),HttpStatus.ACCEPTED);
    }

}
