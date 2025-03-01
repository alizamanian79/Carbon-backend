package com.gym.server.controller;


import com.gym.server.dto.RoleDto;
import com.gym.server.exception.AppForbiddenException;
import com.gym.server.exception.AppNotFoundException;
import com.gym.server.exception.AppSuccessfullException;
import com.gym.server.model.User;
import com.gym.server.service.FileService;
import com.gym.server.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        try {
            List <User> users = userService.getUsers();
            return ResponseEntity.ok(users);
        }catch (Exception e) {
           throw new AppForbiddenException(e.getMessage()) ;
        }

    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User users = userService.updateUser(user);
        return ResponseEntity.ok(users);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping("/role/set")
    public ResponseEntity<User> setRole(@RequestBody RoleDto role) {
        User users = userService.setRole(role);
        return ResponseEntity.ok(users);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> setRole(@PathVariable Long id) {
        String res =userService.deleteUser(id);
        throw new AppSuccessfullException(res);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping("/profile")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Convert the image to Base64
            String base64 = fileService.convertFileToBase64(file);
            // Save Base64 to directory
            String generateName = UUID.randomUUID()+".jpg";
          fileService.saveBase64ToFile(base64, generateName);
            Map<String,String> response = new HashMap<String,String>();
            response.put("message","Image uploaded successfully");
            response.put("status","200");
            response.put("image",generateName);
           return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
        throw new AppNotFoundException(e.getMessage());
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
   @GetMapping("/profile")
    public ResponseEntity<?> showImage(@RequestParam String profileImage) {
        try {
           String base64 = fileService.showProfileBase64(profileImage);
           Map<String,String> res = new HashMap<>();
           res.put("base64",base64);
           return ResponseEntity.ok(res);

        } catch (Exception e) {
            throw new AppNotFoundException(e.getMessage());
        }
    }


}