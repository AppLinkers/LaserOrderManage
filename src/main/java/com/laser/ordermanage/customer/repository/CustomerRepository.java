package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findFirstByUserEmail(String email);
}
