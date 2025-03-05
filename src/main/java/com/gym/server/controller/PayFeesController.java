package com.gym.server.controller;

import com.gym.server.model.PayFees;
import com.gym.server.service.PayFeesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PayFeesController {

    private final PayFeesService payFeesService;


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAll(){
        try {
            return new ResponseEntity<>(payFeesService.getAll(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> add(@RequestBody PayFees payFees){
        try {
            return new ResponseEntity<>(payFeesService.add(payFees), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/{id}")
    public ResponseEntity<?> successPay(@PathVariable Long id){
        try {
            return new ResponseEntity<>(payFeesService.successBuyFees(id), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}
