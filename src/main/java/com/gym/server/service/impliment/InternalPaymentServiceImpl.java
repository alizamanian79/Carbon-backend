package com.gym.server.service.impliment;

import com.gym.server.dto.AccountDto;
import com.gym.server.exception.AppNotFoundException;
import com.gym.server.model.Account;
import com.gym.server.model.Course;
import com.gym.server.model.InternalPayment;
import com.gym.server.repository.AccountRepository;
import com.gym.server.repository.CourseRepository;
import com.gym.server.service.AccountService;
import com.gym.server.service.InternalPaymentRepository;
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
public class InternalPaymentServiceImpl implements InternalPaymentRepository {

    private final com.gym.server.repository.InternalPaymentRepository payFeesRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final CourseRepository feeRepository;

    @Override
    public List<InternalPayment> getAll() {
        return StreamSupport.stream(payFeesRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }


    @Override
    public InternalPayment add(InternalPayment req) {
        Optional<Course> findCourse = feeRepository.findById(req.getCourseId());
        if (!findCourse.isPresent()) {
            throw new AppNotFoundException("Fee not found");
        }
        InternalPayment payFees = new InternalPayment();
        payFees.setAccountId(req.getAccountId());
        payFees.setCourseId(req.getCourseId());
        payFees.setDiscount(req.getDiscount());
        payFees.setAmount(findCourse.get().getAmount() - ((findCourse.get().getAmount()) * findCourse.get().getDiscount() / 100));
        payFees.setStatus("pending");
        payFees.setTransactionId(UUID.randomUUID().toString());
        return payFeesRepository.save(payFees);
    }

    @Override
    public Course update() {
        // Implement update logic or remove if not needed
        return null;
    }

    @Override
    public String delete() {
        // Implement delete logic or remove if not needed
        return "Not implemented";
    }

    @Override
    public Course retrieve(String id) {
        return null;
    }




    //Main


    @Transactional
    @Override
    public InternalPayment successfullInternalPayment(Long id) {
        Optional<InternalPayment> payFees = payFeesRepository.findById(id);
        Optional<Account> account = accountRepository.findById(id);
        if (!payFees.isPresent()) {
            throw new AppNotFoundException("Pay fee not found");
        }


        InternalPayment target = payFees.get();


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
        List<InternalPayment> fees = StreamSupport.stream(payFeesRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();

        for (InternalPayment fee : fees) {
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

