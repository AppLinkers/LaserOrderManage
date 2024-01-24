package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.dto.response.QCustomerGetDeliveryAddressResponse;
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
    public List<CustomerGetDeliveryAddressResponse> findByCustomer(String email) {
        List<CustomerGetDeliveryAddressResponse> customerGetDeliveryAddressResponseList = queryFactory
                .select(new QCustomerGetDeliveryAddressResponse(
                        deliveryAddress.id,
                        deliveryAddress.name,
                        deliveryAddress.address.zipCode,
                        deliveryAddress.address.address,
                        deliveryAddress.address.detailAddress,
                        deliveryAddress.receiver,
                        deliveryAddress.phone1,
                        deliveryAddress.phone2,
                        deliveryAddress.isDefault
                ))
                .from(deliveryAddress)
                .join(deliveryAddress.customer, customer)
                .join(customer.user, userEntity)
                .where(userEntity.email.eq(email))
                .orderBy(deliveryAddress.isDefault.desc(), deliveryAddress.createdAt.desc())
                .fetch();

        return customerGetDeliveryAddressResponseList;
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
