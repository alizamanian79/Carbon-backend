package com.gym.server.controller;

import com.gym.server.model.Course;
import com.gym.server.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService feeService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAllFees() {
        return new ResponseEntity<>(feeService.getAll(), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> addFee(@RequestBody Course req) {
        try {
            return new ResponseEntity<>(feeService.addFee(req), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
       }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<?> updateFee(@RequestBody Course req) {
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
