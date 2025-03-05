package com.gym.server.service;


import com.gym.server.model.Fee;
import com.gym.server.model.PayFees;

import java.util.List;

public interface PayFeesService {
    List<?> getAll();
    PayFees add(PayFees req);
    Fee update();
    String delete();
    Fee retriveFee();


    // main
    PayFees successBuyFees(Long id);

}
