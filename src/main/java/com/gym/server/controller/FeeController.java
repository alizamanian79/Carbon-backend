package com.gym.server.controller;

import com.gym.server.model.Fee;
import com.gym.server.service.FeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/fee")
public class FeeController {

    private final FeeService feeService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAllFees() {
        return new ResponseEntity<>(feeService.getAll(), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> addFee(@RequestBody Fee req) {
        try {
            return new ResponseEntity<>(feeService.addFee(req), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
       }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<?> updateFee(@RequestBody Fee req) {
        try {
            return new ResponseEntity<>(feeService.updateFee(req), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFee(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(feeService.deleteFee(id), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> retriveFee(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(feeService.retriveFee(id), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}
