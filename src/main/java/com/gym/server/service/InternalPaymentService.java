package com.gym.server.service;


import com.gym.server.dto.InternalPayment.InternalPaymentDTO;
import com.gym.server.model.Course;
import com.gym.server.model.InternalPayment;

import java.util.List;

public interface InternalPaymentService {
    List<?> getAll();
    InternalPayment add(InternalPaymentDTO req);
    Course update();
    String delete(Long id);
    Iterable<?> retrieve(Long accountId);


    // main
    InternalPayment successfullInternalPayment(Long id);

}
