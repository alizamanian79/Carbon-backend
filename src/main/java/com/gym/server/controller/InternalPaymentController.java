package com.gym.server.controller;

import com.gym.server.model.InternalPayment;
import com.gym.server.service.InternalPaymentRepository;
import com.gym.server.service.impliment.InternalPaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internalpayment")
public class InternalPaymentController {

    private final InternalPaymentServiceImpl internalPaymentService;


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
    public ResponseEntity<?> add(@RequestBody InternalPayment payFees){
        try {
            return new ResponseEntity<>(internalPaymentService.add(payFees), HttpStatus.OK);
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


}
