package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findFirstByUserEmail(String email);
}
