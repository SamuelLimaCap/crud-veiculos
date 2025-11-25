package com.support.compracarros.services;

import com.support.compracarros.dto.req.CreateUserRequest;
import com.support.compracarros.dto.req.LoginUserRequest;
import com.support.compracarros.dto.res.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public void signUpUser(CreateUserRequest createUserRequest);

    public LoginResponse signInUser(LoginUserRequest loginUserRequest);




}
