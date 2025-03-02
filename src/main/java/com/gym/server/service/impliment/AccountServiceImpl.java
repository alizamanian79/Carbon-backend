package com.gym.server.service.impliment;

import com.gym.server.exception.AppNotFoundException;
import com.gym.server.model.Account;
import com.gym.server.model.User;
import com.gym.server.repository.AccountRepository;
import com.gym.server.repository.UserRepository;
import com.gym.server.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    @Override
    public List<Account> getAccounts() {
        // Convert Iterable to List
        List<Account> accounts = new ArrayList<>();
        accountRepository.findAll().forEach(accounts::add);
        return accounts;
    }

    @Override
    public Account createAccount(Long id) {
        Optional<User> exsit = userRepository.findById(id);
        if (!exsit.isPresent()) {
            throw new AppNotFoundException("کاربر پیدا نشد");
        }
        Account account = new Account();
        account.setUser(exsit.get());
        account.setAmount(20d);
       return accountRepository.save(account);
    }
}
