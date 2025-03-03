package com.gym.server.dto.Zarinpal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestDto {
    private String amount;
    public String mobile;
    public String email;
}
