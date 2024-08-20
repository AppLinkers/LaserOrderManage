package com.laser.ordermanage.factory.repository;

import com.laser.ordermanage.factory.domain.Factory;

import java.util.Optional;

public interface FactoryRepositoryCustom {

    Optional<Factory> findFactoryByFactoryManagerUserEmail(String email);
}
