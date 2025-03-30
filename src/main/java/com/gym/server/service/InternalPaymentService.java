package com.gym.server.service;


import com.gym.server.dto.InternalPayment.InternalPaymentDTO;
import com.gym.server.model.Course;
import com.gym.server.model.InternalPayment;

import java.util.List;
import java.util.Map;

public interface InternalPaymentService {
    List<?> getAll();
    InternalPayment add(Long req);
    String delete(Long id);
    Iterable<?> retrieve();
    InternalPayment getById(Long id);

    // MainActions
    InternalPayment successfullInternalPayment(Long id);
    void callBack(String transactionId , String response);
    InternalPayment getByTransactionId(String transactionId);
    public void isCourseValidate();
    public void callBackDelete(String transactionId);
}
