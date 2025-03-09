package com.gym.server.dto.Zarinpal.verify;

import lombok.Data;

@Data
public class VerifyReqDto {
    private String merchant_id;
    private String amount;
    private String authority;
}


