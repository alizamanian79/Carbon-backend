package com.gym.server.controller;

import com.gym.server.model.Account;
import com.gym.server.dto.AccountDto;
import com.gym.server.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/charge")
    public ResponseEntity<?> chargeAccount(@RequestBody AccountDto accountDto) {
        return accountService.chargeAccount(accountDto);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/deducation")
    public ResponseEntity<?> deducationAccount(@RequestBody AccountDto accountDto) {
        return accountService.deducationAccount(accountDto);

    }

}
