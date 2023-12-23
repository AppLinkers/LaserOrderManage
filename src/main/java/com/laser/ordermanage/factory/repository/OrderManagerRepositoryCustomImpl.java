package com.laser.ordermanage.factory.repository;

import com.laser.ordermanage.factory.dto.response.FactoryGetOrderManagerResponse;
import com.laser.ordermanage.factory.dto.response.QFactoryGetOrderManagerResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.factory.domain.QOrderManager.orderManager;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class OrderManagerRepositoryCustomImpl implements OrderManagerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FactoryGetOrderManagerResponse> findByFactory(String email) {
        List<FactoryGetOrderManagerResponse> factoryGetOrderManagerResponseList = queryFactory
                .select(new QFactoryGetOrderManagerResponse(
                        orderManager.name,
                        orderManager.phone
                ))
                .from(orderManager)
                .join(orderManager.factory, factory)
                .join(factory.user, userEntity)
                .where(userEntity.email.eq(email))
                .orderBy(orderManager.createdAt.desc())
                .fetch();

        return factoryGetOrderManagerResponseList;
    }

    @Override
    public Optional<String> findUserEmailById(Long orderManagerId) {
        String userEmail = queryFactory
                .select(userEntity.email)
                .from(orderManager)
                .join(orderManager.factory, factory)
                .join(factory.user, userEntity)
                .where(orderManager.id.eq(orderManagerId))
                .fetchOne();

        return Optional.ofNullable(userEmail);
    }
}
