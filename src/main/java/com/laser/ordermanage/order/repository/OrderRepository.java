package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long>, OrderRepositoryCustom {

    Optional<Order> findFirstById(Long id);

    void deleteAllByIdIn(List<Long> orderIdList);

}
