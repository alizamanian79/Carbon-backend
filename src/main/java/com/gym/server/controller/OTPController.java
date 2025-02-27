package com.gym.server.controller;

import com.gym.server.model.OTP;
import com.gym.server.service.OTPService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/otp")
public class OTPController {
    private final OTPService otpService;

    public OTPController(OTPService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateOTP(@RequestParam String phoneNumber) {
        OTP otp = otpService.generateOTP(phoneNumber);
        Map<String, String> res = new HashMap<>();
        res.put("otpCode", otp.getOtpCode());
        res.put("message", "OTP generated successfully for " + phoneNumber);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateOTP(@RequestParam String phoneNumber, @RequestParam String otpCode) {
    return otpService.getOTPByPhoneNumber(phoneNumber,otpCode);

    }
}
