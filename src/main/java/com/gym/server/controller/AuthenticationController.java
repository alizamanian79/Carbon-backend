package com.gym.server.controller;

import com.gym.server.dto.LoginResponse;
import com.gym.server.dto.LoginUserDto;
import com.gym.server.dto.RegisterUserDto;
import com.gym.server.exception.AppForbiddenException;
import com.gym.server.model.User;
import com.gym.server.service.impliment.AuthenticationService;
import com.gym.server.service.impliment.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            LoginResponse  token = generateToken(authenticatedUser);

            return ResponseEntity.ok(token);
        }catch (Exception e) {
            throw new AppForbiddenException("نام کاربری یا رمز عبور اشتباه است");
        }




    }


    public LoginResponse generateToken( User authenticatedUser) {

        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return loginResponse;
    }

}