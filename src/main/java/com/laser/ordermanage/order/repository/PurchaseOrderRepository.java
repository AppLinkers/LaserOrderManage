package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.domain.PurchaseOrder;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long> {
}
