package com.laser.ordermanage.factory.repository;

import com.laser.ordermanage.factory.domain.Factory;
import org.springframework.data.repository.CrudRepository;

public interface FactoryRepository extends CrudRepository<Factory, Long>, FactoryRepositoryCustom {

}
