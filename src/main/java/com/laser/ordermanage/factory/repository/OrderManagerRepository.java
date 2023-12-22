package com.laser.ordermanage.factory.repository;

import com.laser.ordermanage.factory.domain.OrderManager;
import org.springframework.data.repository.CrudRepository;

public interface OrderManagerRepository extends CrudRepository<OrderManager, Long> {

}
