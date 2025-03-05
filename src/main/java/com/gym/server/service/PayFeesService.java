package com.gym.server.service;


import com.gym.server.model.Course;
import com.gym.server.model.PayFees;

import java.util.List;

public interface PayFeesService {
    List<?> getAll();
    PayFees add(PayFees req);
    Course update();
    String delete();
    Course retriveFee();


    // main
    PayFees successBuyFees(Long id);

}
