package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.dto.response.QCustomerGetDeliveryAddressResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.laser.ordermanage.customer.domain.QDeliveryAddress.deliveryAddress;

@RequiredArgsConstructor
public class DeliveryAddressRepositoryCustomImpl implements DeliveryAddressRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CustomerGetDeliveryAddressResponse> findByCustomer(String userName) {
        return queryFactory
                .select(new QCustomerGetDeliveryAddressResponse(
                        deliveryAddress.id,
                        deliveryAddress.name,
                        deliveryAddress.zipCode,
                        deliveryAddress.address,
                        deliveryAddress.detailAddress,
                        deliveryAddress.receiver,
                        deliveryAddress.phone1,
                        deliveryAddress.phone2,
                        deliveryAddress.isDefault
                ))
                .from(deliveryAddress)
                .where(deliveryAddress.customer.user.email.eq(userName))
                .orderBy(new OrderSpecifier<>(Order.DESC, deliveryAddress.isDefault))
                .fetch();
    }

}
