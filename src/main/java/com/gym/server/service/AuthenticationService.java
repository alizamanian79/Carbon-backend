package com.gym.server.service;


import com.gym.server.dto.LoginResponse;
import com.gym.server.dto.LoginUserDto;
import com.gym.server.dto.RegisterUserDto;
import com.gym.server.exception.AppExistException;
import com.gym.server.exception.AppForbiddenException;
import com.gym.server.model.User;
import com.gym.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public User signup(RegisterUserDto input) {
        Optional<User> existUser =  userRepository.findByPhoneNumber(input.getPhoneNumber());
        if (existUser.isPresent()) {
            throw new AppExistException("کاربر با این شماره تماس وجود دارد");
        }
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setPasswordDecoder(input.getPassword());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
          Authentication user =  authenticationManager.authenticate(
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