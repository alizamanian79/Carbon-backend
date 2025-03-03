package com.gym.server.dto.Zarinpal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private DataDto data;
    private List<String> errors;
}