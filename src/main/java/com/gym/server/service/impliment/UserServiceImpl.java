package com.gym.server.service.impliment;


import com.gym.server.exception.AppExistException;
import com.gym.server.exception.AppForbiddenException;
import com.gym.server.exception.AppNotFoundException;
import com.gym.server.model.User;
import com.gym.server.repository.UserRepository;
import com.gym.server.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }


    @Override
    @Transactional
    public User updateUser(User user) {
        Optional<User> existUserOpt = userRepository.findById(user.getId());

        if (existUserOpt.isEmpty()) {
            throw new AppNotFoundException("کاربر پیدا نشد");
        }

        User existUser = existUserOpt.get(); // Get the existing user


        // Checking number
        Optional<User> searchingOnNumbers = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (searchingOnNumbers.isPresent()) {
            throw new AppExistException("این شماره متعلق به کاربر دیگری است");
        }

        // Update the existing user's fields
        existUser.setPhoneNumber(user.getPhoneNumber());
        existUser.setFullName(user.getFullName());
        existUser.setEmail(user.getEmail());
        existUser.setPasswordDecoder(user.getPassword());
        existUser.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the new password
        // No need to set phone number again if it's the same

        // Save the updated user
        userRepository.save(existUser);
        return existUser; // Return the updated user
    }




}