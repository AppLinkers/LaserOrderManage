package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.domain.Quotation;
import org.springframework.data.repository.CrudRepository;

public interface QuotationRepository extends CrudRepository<Quotation, Long> {
}
