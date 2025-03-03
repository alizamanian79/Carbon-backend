package com.gym.server.service;

import com.gym.server.dto.Zarinpal.PaymentRequestDto;
import com.gym.server.dto.Zarinpal.PaymentResponseDto;
import com.gym.server.dto.Zarinpal.RequestDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ZarinpalService {
    PaymentResponseDto paymentRequest(RequestDto requestDto);
    void verifyRequest();
}
