package com.laser.ordermanage.user.domain;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.user.domain.type.Role;

public class UserEntityBuilder {

    public static UserEntity build() {
        Address address = Address.builder()
                .zipCode("11111")
                .address("user1_address")
                .detailAddress("user1_detail_address")
                .build();

        return UserEntity.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .name("고객 이름 1")
                .role(Role.ROLE_CUSTOMER)
                .phone("01011111111")
                .address(address)
                .build();
    }

    public static UserEntity newUserBuild() {
        Address address = Address.builder()
                .zipCode("11111")
                .address("address")
                .detailAddress("detail_address")
                .build();

        return UserEntity.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("신규 고객 이름 1")
                .role(Role.ROLE_CUSTOMER)
                .phone("01011111111")
                .address(address)
                .build();
    }
}
