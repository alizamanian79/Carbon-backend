package com.gym.server.service;

import com.gym.server.exception.AppNotFoundException;
import com.gym.server.model.OTP;
import com.gym.server.model.User;
import com.gym.server.repository.OTPRepository;
import com.gym.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class OTPService {
    private final OTPRepository otpRepository;
    private final UserRepository userRepository;



    public OTP generateOTP(String phoneNumber) {
        Optional<OTP> existingOtp = otpRepository.findByPhoneNumber(phoneNumber);
        Optional<User> existingUser = userRepository.findByPhoneNumber(phoneNumber);

        if (!existingOtp.isPresent() && !existingUser.isPresent()) {
            throw new AppNotFoundException("شماره تماس وجود ندارد");
        }




        if (existingOtp.isPresent()) {
            return existingOtp.get();
        }
        String otpCode = String.valueOf(new Random().nextInt(900000) + 100000); // ۶ رقمی
        OTP otp = new OTP(phoneNumber, otpCode);
        return otpRepository.save(otp);
    }

    @Transactional
    public ResponseEntity<String> getOTPByPhoneNumber(String phoneNumber,String otpCode) {
        Optional<OTP> exist = otpRepository.findByPhoneNumber(phoneNumber);
        if (exist.isPresent() && exist.get().getOtpCode().equals(otpCode)) {
            otpRepository.delete(exist.get());
            return ResponseEntity.ok("OTP is valid");
        }
        throw new AppNotFoundException("Invalid OTP Code");
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // هر ۱ دقیقه اجرا می‌شود
    public void deleteExpiredOTPs() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(2);
        otpRepository.deleteAll(otpRepository.findAll()
            .stream()
            .filter(otp -> otp.getCreatedAt().isBefore(expirationTime))
            .toList());
    }
}
