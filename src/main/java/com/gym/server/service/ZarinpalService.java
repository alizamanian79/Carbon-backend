package com.gym.server.service;

import com.gym.server.dto.Zarinpal.PaymentRequestDto;
import com.gym.server.dto.Zarinpal.PaymentResponseDto;
import com.gym.server.dto.Zarinpal.RequestDto;
import com.gym.server.dto.Zarinpal.verify.VerifyReqDto;
import com.gym.server.dto.Zarinpal.verify.VerifyResDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ZarinpalService {
    PaymentResponseDto paymentRequest(RequestDto requestDto , String callBack,String description);
    boolean verify(VerifyReqDto req);
}
