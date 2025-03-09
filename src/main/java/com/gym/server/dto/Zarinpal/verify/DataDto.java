package com.gym.server.dto.Zarinpal.verify;

import lombok.Data;

@Data
public class DataDto {
    private Integer wages;
    private int code;
    private String message;
    private String cardHash;
    private String cardPan;
    private long refId;
    private String feeType;
    private int fee;
    private int shaparakFee;
    private Integer orderId;
}
