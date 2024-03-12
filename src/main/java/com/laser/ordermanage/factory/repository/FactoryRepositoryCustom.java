package com.laser.ordermanage.factory.repository;

import com.laser.ordermanage.factory.domain.Factory;

public interface FactoryRepositoryCustom {

    Factory findFactoryByFactoryManager(String email);
}
