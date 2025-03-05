package com.gym.server.repository;

import com.gym.server.model.PayFees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayFeesRepository extends CrudRepository<PayFees, Long> {
}
