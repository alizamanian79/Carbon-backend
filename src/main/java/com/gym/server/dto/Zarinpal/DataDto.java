package com.gym.server.dto.Zarinpal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataDto {
    private String authority;
    private Long fee;
    private String fee_type;
    private Integer code;
    private String message;
}
