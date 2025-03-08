package com.gym.server.repository;

import com.gym.server.model.InternalPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalPaymentRepository extends CrudRepository<InternalPayment, Long> {
    Iterable<InternalPayment> findByAccountId_Id(Long accountId);
}
