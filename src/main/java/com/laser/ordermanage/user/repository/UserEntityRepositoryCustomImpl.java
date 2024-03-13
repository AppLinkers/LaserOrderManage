package com.laser.ordermanage.user.repository;

import com.laser.ordermanage.user.dto.response.GetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.dto.response.QGetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.QGetUserEmailResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

}
