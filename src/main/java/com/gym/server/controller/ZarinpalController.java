package com.gym.server.controller;

import com.gym.server.dto.Zarinpal.PaymentRequestDto;
import com.gym.server.dto.Zarinpal.PaymentResponseDto;
import com.gym.server.dto.Zarinpal.RequestDto;
import com.gym.server.dto.Zarinpal.verify.VerifyReqDto;
import com.gym.server.dto.Zarinpal.verify.VerifyResDto;
import com.gym.server.service.ZarinpalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zarinpal/payment")
public class ZarinpalController {

    @Value("${app.zarinpal.paymentUrl}")
    private String paymentUrl;

    private final ZarinpalService zarinpalService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> payment(@RequestBody RequestDto req) {

//        return new ResponseEntity<>(paymentRequestDto, HttpStatus.OK);
      PaymentResponseDto response= zarinpalService.paymentRequest(req,"http://alizamanianapp.com");
        String link = paymentUrl+"/"+response.getData().getAuthority().toString();
        return new ResponseEntity<>(link, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyReqDto req) {
    VerifyResDto res = zarinpalService.verify(req);
        if (res.getData().getCode()==100){
            return new ResponseEntity<>("this transaction has valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("this transaction has not real", HttpStatus.BAD_REQUEST);
    }

}
