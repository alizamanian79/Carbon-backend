package com.gym.server.service;

import com.gym.server.model.Account;
import com.gym.server.model.User;

import java.util.List;

public interface AccountService {
    List<Account> getAccounts();
    Account createAccount(Long id);
}
