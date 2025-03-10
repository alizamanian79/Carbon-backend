package com.gym.server.service;


import com.gym.server.dto.InternalPayment.InternalPaymentDTO;
import com.gym.server.model.Course;
import com.gym.server.model.InternalPayment;

import java.util.List;
import java.util.Map;

public interface InternalPaymentService {
    List<?> getAll();
    InternalPayment add(InternalPaymentDTO req);
    String delete(Long id);
    Iterable<?> retrieve(Long accountId);
    InternalPayment getById(Long id);

    // MainActions
    InternalPayment successfullInternalPayment(Long id);
    InternalPayment callBack(String transactionId , String response);
    InternalPayment getByTransactionId(String transactionId);
}
