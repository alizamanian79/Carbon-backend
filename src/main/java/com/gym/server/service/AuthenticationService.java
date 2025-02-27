package com.gym.server.service;


import com.gym.server.dto.LoginUserDto;
import com.gym.server.dto.RegisterUserDto;
import com.gym.server.exception.AppExistException;
import com.gym.server.model.User;
import com.gym.server.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        Optional<User> existUser =  userRepository.findByPhoneNumber(input.getPhoneNumber());
        if (existUser.isPresent()) {
            throw new AppExistException("کاربر با این شماره تماس وجود دارد");
        }
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
            // Authenticate using phone number and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getPhoneNumber(),
                            input.getPassword()
                    )
            );
        // Retrieve and return the user from the repository
        return userRepository.findByPhoneNumber(input.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}