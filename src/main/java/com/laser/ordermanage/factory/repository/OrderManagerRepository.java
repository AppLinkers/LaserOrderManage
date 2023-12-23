package com.laser.ordermanage.factory.repository;

import com.laser.ordermanage.factory.domain.OrderManager;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderManagerRepository extends CrudRepository<OrderManager, Long>, OrderManagerRepositoryCustom {

    Optional<OrderManager> findFirstById(Long orderManagerId);

}
