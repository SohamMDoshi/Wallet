package com.swiggy.wallet.controller;

import com.swiggy.wallet.dto.UserRegistrationRequest;
import com.swiggy.wallet.entity.Users;
import com.swiggy.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody UserRegistrationRequest request) {
        Users users = userService.registerUser(request.getUsername(), request.getPassword());
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }
}
