package com.gym.server.dto.Zarinpal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentRequestDto {
    private String merchant_id;
    private String amount;
    private String callback_url;
    private String description;
    private MetadataDto metadata;
    private String currency;
}
