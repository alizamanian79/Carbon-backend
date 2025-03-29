package com.gym.server.controller;

import com.gym.server.dto.LoginResponse;
import com.gym.server.dto.LoginUserDto;
import com.gym.server.dto.RegisterUserDto;
import com.gym.server.exception.AppBadRequest;
import com.gym.server.exception.AppExistException;
import com.gym.server.model.User;
import com.gym.server.service.AuthenticationService;
import com.gym.server.service.impliment.AuthenticationServiceImpl;
import com.gym.server.service.impliment.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationServiceImpl authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto registerUserDto,
                                         BindingResult bindingResult) throws AppBadRequest, AppExistException {
        if (bindingResult.hasErrors()) {
          throw new AppBadRequest(bindingResult.getFieldError().getDefaultMessage());
        }

            User registeredUser = authenticationService.signup(registerUserDto);
            return ResponseEntity.ok(registeredUser);


    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginUserDto loginUserDto,BindingResult bindingResult) {

        // check validation
        if (bindingResult.hasErrors()) {
            throw new AppBadRequest(bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            LoginResponse  token = generateToken(authenticatedUser);
            return ResponseEntity.ok(token);
        }catch (Exception e) {
            throw new AppBadRequest("نام کاربری یا رمز عبور اشتباه است");
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