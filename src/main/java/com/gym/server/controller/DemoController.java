package com.gym.server.controller;

import com.gym.server.exception.AppForbiddenException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/demo")
public class DemoController {


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/user")
    public String userAccess() {
        try {
            return "Hello World";
        }catch (Exception e) {
            throw new AppForbiddenException("Forbidden");
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminAccess() {
        try {
            return "Hello World";
        }catch (Exception e) {
            throw new AppForbiddenException("Forbidden");
        }

    }

}
