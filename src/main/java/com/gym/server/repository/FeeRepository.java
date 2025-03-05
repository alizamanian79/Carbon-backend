package com.gym.server.repository;

import com.gym.server.model.Fee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends CrudRepository<Fee, Long> {
}
