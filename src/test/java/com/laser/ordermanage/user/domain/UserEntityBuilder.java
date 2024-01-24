package com.laser.ordermanage.user.domain;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.user.domain.type.Role;

public class UserEntityBuilder {

    public static UserEntity build() {
        Address address = Address.builder()
                .zipCode("11111")
                .address("고객 주소")
                .detailAddress("고객 상세 주소")
                .build();

        return UserEntity.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .role(Role.ROLE_CUSTOMER)
                .phone("01011111111")
                .address(address)
                .build();
    }
}
