package com.gym.server.repository;

import com.gym.server.model.InternalPayment;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternalPaymentRepository extends CrudRepository<InternalPayment, Long> {
    Iterable<InternalPayment> findByAccountId_Id(Long accountId);
    InternalPayment findByTransactionId(String transactionId, Sort sort);

    Optional<InternalPayment> findByTransactionId(String id);

    void deleteByTransactionId(String transactionId);
}
