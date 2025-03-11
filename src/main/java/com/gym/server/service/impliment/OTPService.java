package com.gym.server.service.impliment;

import com.gym.server.dto.LoginResponse;
import com.gym.server.dto.LoginUserDto;
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
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;


    public OTP generateOTP(String phoneNumber) {
        Optional<OTP> existingOtp = otpRepository.findByPhoneNumber(phoneNumber);
        Optional<User> existingUser = userRepository.findByPhoneNumber(phoneNumber);

        if (!existingOtp.isPresent() && !existingUser.isPresent()) {
            throw new AppNotFoundException("شماره تماس وجود ندارد");
        }




        if (existingOtp.isPresent()) {
            return existingOtp.get();
        }
        String otpCode = String.valueOf(new Random().nextInt(9000) + 1000); // ۶ رقمی
        OTP otp = new OTP(phoneNumber, otpCode);
        return otpRepository.save(otp);
    }

    @Transactional
    public ResponseEntity<?> getOTPByPhoneNumber(String phoneNumber,String otpCode) {
        Optional<OTP> exist = otpRepository.findByPhoneNumber(phoneNumber);
        if (exist.isPresent() && exist.get().getOtpCode().equals(otpCode)) {

         String password = userRepository.findByPhoneNumber(phoneNumber).get().getPasswordDecoder();

            LoginUserDto loginUserDto = new LoginUserDto();
            loginUserDto.setPhoneNumber(phoneNumber);
            loginUserDto.setPassword(password);

            User authentication = authenticationService.authenticate(loginUserDto);
            LoginResponse token = generateToken(authentication);

            otpRepository.delete(exist.get());
            return ResponseEntity.ok(token);
        }
        throw new AppNotFoundException("کد نامعتبر میباشد");
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

    public LoginResponse generateToken(User authenticatedUser) {

        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return loginResponse;
    }
}
