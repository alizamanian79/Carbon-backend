package com.gym.server.dto.InternalPayment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InternalPaymentDTO {
//    private Long accountId;
    private Long courseId;
}
