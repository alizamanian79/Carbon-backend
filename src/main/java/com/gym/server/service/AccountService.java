package com.gym.server.service;

import com.gym.server.model.Account;
import com.gym.server.dto.AccountDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    List<Account> getAccounts();
    Account createAccount(Long id);
    ResponseEntity<?> chargeAccount(AccountDto accountDto);
    ResponseEntity<?> deducationAccount(AccountDto accountDto);

}
