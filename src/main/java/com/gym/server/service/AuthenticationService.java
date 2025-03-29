package com.gym.server.service;

import com.gym.server.dto.LoginUserDto;
import com.gym.server.dto.RegisterUserDto;
import com.gym.server.exception.AppExistException;
import com.gym.server.model.User;

public interface AuthenticationService {
    public User signup(RegisterUserDto input);
    public User authenticate(LoginUserDto input);
    public User me();

}
