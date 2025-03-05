package com.gym.server.service;

import com.gym.server.model.Fee;

import java.util.List;

public interface FeeService {
    List<?> getAll();
    Fee addFee(Fee req);
    Fee updateFee(Fee req);
    String deleteFee(Long id);
    Fee retriveFee(Long id);

}
