package com.gym.server.repository;

import com.gym.server.model.InternalPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalPaymentRepository extends CrudRepository<InternalPayment, Long> {
}
