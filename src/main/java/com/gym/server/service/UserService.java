package com.gym.server.service;

import com.gym.server.dto.RoleDto;
import com.gym.server.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User updateUser(User user);
    User setRole(RoleDto req);
    String deleteUser(Long id);
}
