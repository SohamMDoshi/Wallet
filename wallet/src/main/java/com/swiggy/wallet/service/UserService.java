package com.swiggy.wallet.service;

import com.swiggy.wallet.entity.Users;

public interface UserService {

    public Users registerUser(String username, String password);
}
