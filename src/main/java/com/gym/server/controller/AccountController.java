package com.gym.server.controller;

import com.gym.server.model.Account;
import com.gym.server.model.User;
import com.gym.server.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PostMapping("{id}")
//    public String createAccount(@RequestParam Long id) {
//         accountService.createAccount(id);
//        return"Account created";
//    }

}
