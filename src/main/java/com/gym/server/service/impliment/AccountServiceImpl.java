package com.gym.server.service.impliment;

import com.gym.server.exception.AppNotFoundException;
import com.gym.server.model.Account;
import com.gym.server.dto.AccountDto;
import com.gym.server.model.User;
import com.gym.server.repository.AccountRepository;
import com.gym.server.repository.UserRepository;
import com.gym.server.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
        account.setAmount(0l);
       return accountRepository.save(account);
    }

    @Override
    public ResponseEntity<?> chargeAccount(AccountDto accountDto) {
        Optional<User> existUser = userRepository.findById(accountDto.getId());
        if (!existUser.isPresent()) {
            throw new AppNotFoundException("کاربر پیدا نشد");
        }

        // Get the user's account
        Account account = existUser.get().getAccount();

        if (account == null) {
            throw new AppNotFoundException("حساب کاربری پیدا نشد");
        }

        // Update the account amount
        account.setAmount(account.getAmount() + accountDto.getAmount());

        // Save the updated account
        accountRepository.save(account);

        Map res = new HashMap<>();
        res.put("message", "حساب شما مبلغ"+ accountDto.getAmount()+"شارژ شد");
        res.put("AccountAmount",account.getAmount().toString());

        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @Override
    public Boolean deducationAccount(AccountDto accountDto) {
        Optional<User> existUser = userRepository.findById(accountDto.getId());
        if (!existUser.isPresent()) {
            throw new AppNotFoundException("کاربر پیدا نشد");
        }

        // Get the user's account
        Account account = existUser.get().getAccount();

        if (account == null) {
            throw new AppNotFoundException("حساب کاربری پیدا نشد");
        }

        // Update the account amount
        Map<String,String> res = new HashMap<>();
        if (account.getAmount() - accountDto.getAmount() < 0) {
//            res.put("message","موجودی حساب شما برای این تراکنش کافی نمیباشد از لینک زیر برای شارژ حساب خود استفاده کنید");
//            res.put("AccountAmount",account.getAmount().toString());
//            res.put("url","/charge");
           return false ;
        }
        account.setAmount(account.getAmount() - accountDto.getAmount());

        // Save the updated account
        accountRepository.save(account);
//        res.put("message", "حساب شما مبلغ"+ accountDto.getAmount()+"کسر شد");
//        res.put("AccountAmount",account.getAmount().toString());


        return true;
    }


}
