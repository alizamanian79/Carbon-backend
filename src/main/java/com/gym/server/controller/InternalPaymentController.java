package com.gym.server.controller;

import com.gym.server.dto.InternalPayment.InternalPaymentDTO;
import com.gym.server.model.InternalPayment;
import com.gym.server.repository.InternalPaymentRepository;
import com.gym.server.service.impliment.InternalPaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internalpayment")
public class InternalPaymentController {

    private final InternalPaymentServiceImpl internalPaymentService;
    private final InternalPaymentRepository internalPaymentRepository;


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAll(){
        try {
            return new ResponseEntity<>(internalPaymentService.getAll(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> add(@RequestBody InternalPaymentDTO req){
        try {
            return new ResponseEntity<>(internalPaymentService.add(req), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/{id}")
    public ResponseEntity<?> successPay(@PathVariable Long id){
        try {
            return new ResponseEntity<>(internalPaymentService.successfullInternalPayment(id), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id){

            return new ResponseEntity<>(internalPaymentService.delete(id), HttpStatus.OK);
    }

    @Scheduled(fixedRate = 3600000*24)  // This runs every 24hour
    public void runEveryMinute() {
        Iterable<InternalPayment> list = internalPaymentRepository.findAll(); // Assume InternalPayment is your entity class

        for (InternalPayment payment : list) {
            if ("pending".equals(payment.getStatus())) {  // Corrected the method call
                internalPaymentRepository.deleteById(payment.getId());
            }
        }
    }

}
