package com.gym.server.service.impliment;

import com.gym.server.dto.AccountDto;
import com.gym.server.dto.InternalPayment.InternalPaymentDTO;
import com.gym.server.exception.AppNotFoundException;
import com.gym.server.model.Account;
import com.gym.server.model.Course;
import com.gym.server.model.InternalPayment;
import com.gym.server.repository.AccountRepository;
import com.gym.server.repository.CourseRepository;
import com.gym.server.repository.InternalPaymentRepository;
import com.gym.server.service.AccountService;
import com.gym.server.service.InternalPaymentService;
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
public class InternalPaymentServiceImpl implements InternalPaymentService {

    private final InternalPaymentRepository internalPaymentRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final CourseRepository courseRepository;

    @Override
    public List<InternalPayment> getAll() {
        return StreamSupport.stream(internalPaymentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }


    @Override
    public InternalPayment add(InternalPaymentDTO req) {
        Optional<Course> findCourse = courseRepository.findById(req.getCourseId());
        Optional<Account> findAccount = accountRepository.findById(req.getAccountId());
        if (!findCourse.isPresent()) {
            throw new AppNotFoundException("دوره یافت نشد");
        }

        InternalPayment payment = new InternalPayment();
        payment.setAccountId(findAccount.get());
        payment.setCourseId(findCourse.get());
        payment.setDiscount(findCourse.get().getDiscount());
        payment.setAmount(findCourse.get().getAmount() - ((findCourse.get().getAmount()) * findCourse.get().getDiscount() / 100));
        payment.setStatus("pending");

        String code = String.valueOf(new Random().nextInt(90000) + 10000); // ۶ رقمی
        payment.setTransactionId(code);
        return internalPaymentRepository.save(payment);
    }

    @Override
    public String delete(Long id) {
        Optional<InternalPayment> findInternalPayment = internalPaymentRepository.findById(id);
        if (!findInternalPayment.isPresent()) {
            throw new AppNotFoundException("دوره درون برنامه پیدا نشد");
        }
        internalPaymentRepository.deleteById(id);
        return "برنامه حذف شد";
    }

    @Override
    public Iterable<?> retrieve(Long accountId) {
       return internalPaymentRepository.findByAccountId_Id(accountId);
    }

    @Override
    public InternalPayment getById(Long id) {
       Optional<InternalPayment> find =  internalPaymentRepository.findById(id);
       if (!find.isPresent()) {
           throw new AppNotFoundException("Not found");
       }
       return find.get();
    }


    //Main


    @Transactional
    @Override
    public InternalPayment successfullInternalPayment(Long id) {
        Optional<InternalPayment> findPayment = internalPaymentRepository.findById(id);
//        Optional<Account> account = accountRepository.findById(id);
        if (!findPayment.isPresent()) {
            throw new AppNotFoundException("دوره پیدا نشد");
        }


        InternalPayment target = findPayment.get();


        Optional<Account> userAccount = accountRepository.findById(target.getAccountId().getId());
        AccountDto accountDto = new AccountDto();
        accountDto.setId(userAccount.get().getId());
        accountDto.setAmount(target.getAmount());
        Boolean checkAccount = accountService.deducationAccount(accountDto);
        if (checkAccount==false) {
            throw new AppNotFoundException("موجودی کافی نمیباشد");
        }

        // Only update if not expired
        if (!target.getStatus().equals("expired")) {
            target.setStatus("OK");
            target.setStartAt(LocalDateTime.now());
            target.setEndedAt(target.getStartAt().plusDays(30));
            internalPaymentRepository.save(target);

        }

        return target;
    }

    @Transactional
    @Override
    public InternalPayment callBack(String transactionId, String response) {
        // Find the payment record by transaction ID
        Optional<InternalPayment> optionalPayment = internalPaymentRepository.findByTransactionId(transactionId);

        // Check if the payment record exists
        if (optionalPayment.isPresent()) {
            var find = optionalPayment.get();

            // Check API response
            if (response.equals("OK")) {
                find.setStatus("OK");
                find.setStartAt(LocalDateTime.now());
                find.setEndedAt(find.getStartAt().plusDays(30));
                internalPaymentRepository.save(find); // Ensure to save the updated entity
                return find; // Return the updated payment
            } else {
                // Handle other response cases if necessary
                find.setStatus("pending"); // Example status for other responses
                internalPaymentRepository.save(find); // Save the updated entity
                return find; // Return the updated payment
            }
        } else {
            // Handle the case where the payment is not found
            throw new AppNotFoundException("پیدا نشد");
        }
    }

    @Override
    public InternalPayment getByTransactionId(String transactionId) {
      return   internalPaymentRepository.findByTransactionId(transactionId).get();
    }







    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void updateExpiredFees() {
        // Convert Iterable to List
        List<InternalPayment> fees = StreamSupport.stream(internalPaymentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();

        for (InternalPayment fee : fees) {
            // Check if endedAt is not null before comparing
            if (fee.getEndedAt() != null && now.isAfter(fee.getEndedAt())) {
                fee.setStatus("expired");
            }
        }

        internalPaymentRepository.saveAll(fees); // Save all updated fees
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

