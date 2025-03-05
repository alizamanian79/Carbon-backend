package com.gym.server.service;


import com.gym.server.model.Course;
import com.gym.server.model.InternalPayment;

import java.util.List;

public interface InternalPaymentRepository {
    List<?> getAll();
    InternalPayment add(InternalPayment req);
    Course update();
    String delete();
    Course retrieve(String id);


    // main
    InternalPayment successfullInternalPayment(Long id);

}
