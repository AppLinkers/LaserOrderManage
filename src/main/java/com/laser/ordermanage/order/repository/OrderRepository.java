package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long>, OrderRepositoryCustom {
}
