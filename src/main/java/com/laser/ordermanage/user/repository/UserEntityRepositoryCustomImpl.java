package com.laser.ordermanage.user.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetUserAccountResponse;
import com.laser.ordermanage.customer.dto.response.QCustomerGetUserAccountResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetUserAccountResponse;
import com.laser.ordermanage.factory.dto.response.QFactoryGetUserAccountResponse;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.response.GetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.dto.response.QGetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.QGetUserEmailResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.laser.ordermanage.customer.domain.QCustomer.customer;
import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.factory.domain.QFactoryManager.factoryManager;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class UserEntityRepositoryCustomImpl implements UserEntityRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetUserEmailResponse> findEmailByNameAndPhone(String name, String phone) {
        List<GetUserEmailResponse> getUserEmailResponseList = queryFactory
                .select(new QGetUserEmailResponse(
                        userEntity.name,
                        userEntity.email
                ))
                .from(userEntity)
                .where(
                        userEntity.name.eq(name),
                        userEntity.phone.eq(phone)
                )
                .orderBy(userEntity.createdAt.desc())
                .fetch();

        return getUserEmailResponseList;
    }

    @Override
    public GetUserAccountResponse findUserAccountByEmail(String email) {
        GetUserAccountResponse getUserAccountResponse = queryFactory
                .select(new QGetUserAccountResponse(
                    userEntity.email,
                        userEntity.name,
                        userEntity.phone,
                        userEntity.address.zipCode,
                        userEntity.address.address,
                        userEntity.address.detailAddress,
                        userEntity.emailNotification
                ))
                .from(userEntity)
                .where(userEntity.email.eq(email))
                .fetchOne();

        return getUserAccountResponse;
    }

    @Override
    public FactoryGetUserAccountResponse findUserAccountByFactory(String email) {
        FactoryGetUserAccountResponse factoryGetUserAccountResponse = queryFactory
                .select(new QFactoryGetUserAccountResponse(
                        userEntity.email,
                        factory.companyName,
                        factory.representative,
                        userEntity.phone,
                        factory.fax,
                        userEntity.address.zipCode,
                        userEntity.address.address,
                        userEntity.address.detailAddress,
                        userEntity.emailNotification
                ))
                .from(userEntity)
                .join(factoryManager).on(factoryManager.user.eq(userEntity))
                .join(factory).on(factoryManager.factory.eq(factory))
                .where(
                        userEntity.email.eq(email),
                        userEntity.role.eq(Role.ROLE_FACTORY)
                )
                .fetchOne();

        return factoryGetUserAccountResponse;
    }

    @Override
    public CustomerGetUserAccountResponse findUserAccountByCustomer(String email) {
        CustomerGetUserAccountResponse customerGetUserAccountResponse = queryFactory
                .select(new QCustomerGetUserAccountResponse(
                        userEntity.email,
                        userEntity.name,
                        userEntity.phone,
                        userEntity.address.zipCode,
                        userEntity.address.address,
                        userEntity.address.detailAddress,
                        customer.companyName,
                        userEntity.emailNotification
                ))
                .from(userEntity)
                .join(customer).on(customer.user.eq(userEntity))
                .where(
                        userEntity.email.eq(email),
                        userEntity.role.eq(Role.ROLE_CUSTOMER)
                )
                .fetchOne();

        return customerGetUserAccountResponse;
    }
}
