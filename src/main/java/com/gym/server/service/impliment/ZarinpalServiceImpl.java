package com.gym.server.service.impliment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.server.dto.Zarinpal.MetadataDto;
import com.gym.server.dto.Zarinpal.PaymentRequestDto;
import com.gym.server.dto.Zarinpal.PaymentResponseDto;
import com.gym.server.dto.Zarinpal.RequestDto;
import com.gym.server.dto.Zarinpal.verify.VerifyReqDto;
import com.gym.server.dto.Zarinpal.verify.VerifyResDto;
import com.gym.server.exception.AppNotFoundException;
import com.gym.server.service.ZarinpalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZarinpalServiceImpl implements ZarinpalService {

    @Value("${app.zarinpal.paymentEndpoint}")
    private String paymentEndpoint;

    @Value("${app.zarinpal.merchantId}")
    private String merchantId;

    @Value("${app.zarinpal.paymentUrl}")
    private String paymentUrl;

    @Value("${app.zarinpal.verifyUrl}")
    private String verifyUrl;


    @Autowired
    private RestTemplate restTemplate;


//    @Override
//    public Map<String, Object> paymentRequest(PaymentRequestDto paymentRequest) {
//        String url = "https://sandbox.zarinpal.com/pg/v4/payment/request.json";
//
//        // Set headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Accept", "application/json");
//
//        // Create the request entity
//        HttpEntity<?> requestEntity = new HttpEntity<>(paymentRequest, headers);
//
//        // Make the request
//        ResponseEntity<String> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
//
//        // Convert the response body to a Map
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> responseMap = new HashMap<>();
//
//        try {
//            responseMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {});
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            responseMap.put("error", "Failed to parse response");
//        }
//
//        // Add the status code to the Map
//        responseMap.put("status", responseEntity.getStatusCodeValue());
//
//        // Return the response Map
//        return responseMap;
//    }
//



    @Override
    public PaymentResponseDto paymentRequest(RequestDto request,String callBack) {

//        myapp.com?Authority=S000000000000000000000000000000678mo&Status=NOK
        PaymentRequestDto customPayment = new PaymentRequestDto();
        customPayment.setMerchant_id(merchantId);
        customPayment.setAmount(request.getAmount());
        customPayment.setCallback_url(callBack);
        customPayment.setDescription("Transaction description.");
        //MetaData
        MetadataDto metadata = new MetadataDto();
        metadata.setMobile(request.getMobile());
        metadata.setEmail(request.getEmail());
        customPayment.setMetadata(metadata);



        String url = paymentEndpoint;
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        // Create the request entity
        HttpEntity<?> requestEntity = new HttpEntity<>(customPayment, headers);

        // Make the request
        ResponseEntity<PaymentResponseDto> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                PaymentResponseDto.class
        );

        PaymentResponseDto paymentResponseDto = responseEntity.getBody();

        try {
         return paymentResponseDto;
        } catch (Exception e) {
            throw new AppNotFoundException("Error while reading response body");
        }


    }

    @Override
    public VerifyResDto verify(VerifyReqDto req) {
        VerifyReqDto verifyReqDto = new VerifyReqDto();
        verifyReqDto.setMerchant_id(merchantId);
        verifyReqDto.setAmount(req.getAmount());
        verifyReqDto.setAuthority(req.getAuthority());


        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        // Create the request entity
        HttpEntity<?> requestEntity = new HttpEntity<>(verifyReqDto, headers);

        String url =verifyUrl;
        // Make the request
        ResponseEntity<VerifyResDto> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                VerifyResDto.class
        );

        VerifyResDto res = responseEntity.getBody();

        return res;

    }



}
