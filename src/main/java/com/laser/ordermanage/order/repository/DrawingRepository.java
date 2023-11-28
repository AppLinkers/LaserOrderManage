package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.domain.Drawing;
import org.springframework.data.repository.CrudRepository;

public interface DrawingRepository extends CrudRepository<Drawing, Long> {
}
