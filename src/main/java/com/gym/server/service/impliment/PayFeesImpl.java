package com.gym.server.service.impliment;

import com.gym.server.dto.AccountDto;
import com.gym.server.exception.AppNotFoundException;
import com.gym.server.model.Account;
import com.gym.server.model.Fee;
import com.gym.server.model.PayFees;
import com.gym.server.repository.AccountRepository;
import com.gym.server.repository.FeeRepository;
import com.gym.server.repository.PayFeesRepository;
import com.gym.server.service.AccountService;
import com.gym.server.service.PayFeesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayFeesImpl implements PayFeesService {

    private final PayFeesRepository payFeesRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final FeeRepository feeRepository;

    @Override
    public List<PayFees> getAll() {
        return StreamSupport.stream(payFeesRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }


    @Override
    public PayFees add(PayFees req) {
        Optional<Fee> findFee = feeRepository.findById(req.getFeeId());
        if (!findFee.isPresent()) {
            throw new AppNotFoundException("Fee not found");
        }
        PayFees payFees = new PayFees();
        payFees.setAccountId(req.getAccountId());
        payFees.setFeeId(req.getFeeId());
        payFees.setDiscount(req.getDiscount());
        payFees.setAmount(findFee.get().getAmount() - ((findFee.get().getAmount()) * findFee.get().getDiscount() / 100));
        payFees.setStatus("pending");
        payFees.setTransactionId(UUID.randomUUID().toString());
        return payFeesRepository.save(payFees);
    }

    @Override
    public Fee update() {
        // Implement update logic or remove if not needed
        return null;
    }

    @Override
    public String delete() {
        // Implement delete logic or remove if not needed
        return "Not implemented";
    }

    @Override
    public Fee retriveFee() {
        // Implement retrieve logic or remove if not needed
        return null;
    }


    //Main


    @Transactional
    @Override
    public PayFees successBuyFees(Long id) {
        Optional<PayFees> payFees = payFeesRepository.findById(id);
        Optional<Account> account = accountRepository.findById(id);
        if (!payFees.isPresent()) {
            throw new AppNotFoundException("Pay fee not found");
        }


        PayFees target = payFees.get();


        Optional<Account> userAccount = accountRepository.findById(target.getAccountId());
        AccountDto accountDto = new AccountDto();
        accountDto.setId(userAccount.get().getId());
        accountDto.setAmount(target.getAmount());
        Boolean checkAccount = accountService.deducationAccount(accountDto);
        if (checkAccount==false) {
            throw new AppNotFoundException("وجودی کافی نمیباشد");
        }

        // Only update if not expired
        if (!target.getStatus().equals("expired")) {
            target.setStatus("Ok");
            target.setStartAt(LocalDateTime.now());
            target.setEndedAt(target.getStartAt().plusDays(30));
            payFeesRepository.save(target);

        }

        return payFees.get();
    }





    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void updateExpiredFees() {
        // Convert Iterable to List
        List<PayFees> fees = StreamSupport.stream(payFeesRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();

        for (PayFees fee : fees) {
            // Check if endedAt is not null before comparing
            if (fee.getEndedAt() != null && now.isAfter(fee.getEndedAt())) {
                fee.setStatus("expired");
            }
        }

        payFeesRepository.saveAll(fees); // Save all updated fees
    }



    public static String convertToPersianDate(String startAt) {
        // Define the formatter for the incoming date
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;

        // Parse the input string to LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse(startAt, isoFormatter);

        // Define the Persian date formatter
        DateTimeFormatter persianFormatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy/MM/dd HH:mm:ss")
                .toFormatter(Locale.forLanguageTag("fa"));

        // Format the LocalDateTime to Persian date string
        return startDateTime.format(persianFormatter);
    }



}

