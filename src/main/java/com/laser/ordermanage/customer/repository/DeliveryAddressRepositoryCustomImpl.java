package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.dto.response.GetDeliveryAddressResponse;
import com.laser.ordermanage.customer.dto.response.QGetDeliveryAddressResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.laser.ordermanage.customer.domain.QCustomer.customer;
import static com.laser.ordermanage.customer.domain.QDeliveryAddress.deliveryAddress;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class DeliveryAddressRepositoryCustomImpl implements DeliveryAddressRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetDeliveryAddressResponse> findByCustomer(String userName) {
        List<GetDeliveryAddressResponse> getDeliveryAddressResponseList = queryFactory
                .select(new QGetDeliveryAddressResponse(
                        deliveryAddress.id,
                        deliveryAddress.name,
                        deliveryAddress.zipCode,
                        deliveryAddress.address,
                        deliveryAddress.detailAddress,
                        deliveryAddress.receiver,
                        deliveryAddress.phone1,
                        deliveryAddress.phone2,
                        deliveryAddress.isDefault,
                        deliveryAddress.isDeleted
                ))
                .from(deliveryAddress)
                .join(deliveryAddress.customer, customer)
                .join(customer.user, userEntity)
                .where(userEntity.email.eq(userName))
                .orderBy(deliveryAddress.isDefault.desc(), deliveryAddress.createdAt.desc())
                .fetch();

        return getDeliveryAddressResponseList;
    }

    @Override
    public Optional<String> findUserEmailById(Long deliveryAddressId) {
        String userEmail = queryFactory
                .select(userEntity.email)
                .from(deliveryAddress)
                .join(deliveryAddress.customer, customer)
                .join(customer.user, userEntity)
                .where(deliveryAddress.id.eq(deliveryAddressId))
                .fetchOne();

        return Optional.ofNullable(userEmail);
    }

}
