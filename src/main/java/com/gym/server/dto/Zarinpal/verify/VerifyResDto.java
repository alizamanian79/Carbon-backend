package com.gym.server.dto.Zarinpal.verify;

import lombok.Data;

import java.util.List;

@Data
public class VerifyResDto {
    private DataDto data;
    private List<String> errors;
}
