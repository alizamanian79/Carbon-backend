package com.gym.server.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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


    @GetMapping("/user")
    public String userAccess() {
        try {
            return "Hello World";
        }catch (Exception e) {
            throw new AppForbiddenException("Forbidden");
        }
    }

    @GetMapping("/admin")
    public String adminAccess() {
        try {
            return "Hello World";
        }catch (Exception e) {
            throw new AppForbiddenException("Forbidden");
        }

    }


    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "سلام، خوش آمدید!");
        return "hello"; // نام فایل HTML بدون پسوند
    }


}
