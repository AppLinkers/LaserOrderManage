package com.laser.ordermanage.factory.repository;

import com.laser.ordermanage.factory.domain.Factory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.factory.domain.QFactoryManager.factoryManager;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class FactoryRepositoryCustomImpl implements FactoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Factory> findFactoryByFactoryManagerUserEmail(String email) {
        return Optional.ofNullable(queryFactory
                .selectFrom(factory)
                .join(factoryManager).on(factoryManager.factory.eq(factory))
                .join(factoryManager.user, userEntity)
                .where(userEntity.email.eq(email))
                .fetchOne());
    }
}
