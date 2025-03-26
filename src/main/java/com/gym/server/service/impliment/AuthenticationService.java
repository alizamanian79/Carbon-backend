package com.gym.server.service.impliment;


import com.gym.server.dto.LoginUserDto;
import com.gym.server.dto.RegisterUserDto;
import com.gym.server.exception.AppBadRequest;
import com.gym.server.exception.AppExistException;
import com.gym.server.model.Role;
import com.gym.server.model.User;
import com.gym.server.repository.UserRepository;
import com.gym.server.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;


    @Transactional
    public User signup(RegisterUserDto input) throws AppExistException {
        Optional<User> existUser =  userRepository.findByPhoneNumber(input.getPhoneNumber());
        if (existUser.isPresent()) {
            throw new AppExistException("کاربر با این شماره تماس وجود دارد");
        }
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setPasswordDecoder(input.getPassword());


        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);  // Assigning USER role


        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        User result = userRepository.save(user);


        result.setAccount(accountService.createAccount(result.getId()));
        userRepository.save(result);

        return result;
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