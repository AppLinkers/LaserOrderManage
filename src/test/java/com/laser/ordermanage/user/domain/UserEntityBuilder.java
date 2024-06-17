package com.laser.ordermanage.user.domain;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.user.domain.type.Authority;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.domain.type.SignupMethod;
import org.assertj.core.api.Assertions;

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
                .authority(Authority.AUTHORITY_ADMIN)
                .phone("01022221111")
                .address(address)
                .signupMethod(SignupMethod.BASIC)
                .build();
    }

    public static UserEntity factoryAdminUserBuild() {
        Address address = Address.builder()
                .zipCode("11111")
                .address("factory1_address")
                .detailAddress("factory1_detail_address")
                .build();

        return UserEntity.builder()
                .email("admin@kumoh.org")
                .password("factory1-password")
                .name("관리자")
                .role(Role.ROLE_FACTORY)
                .authority(Authority.AUTHORITY_ADMIN)
                .phone("01011111111")
                .address(address)
                .signupMethod(SignupMethod.BASIC)
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
                .authority(Authority.AUTHORITY_ADMIN)
                .phone("01011111111")
                .address(address)
                .signupMethod(SignupMethod.BASIC)
                .build();
    }

    public static void assertUserEntity(UserEntity actualUserEntity, UserEntity expectedUserEntity) {
        Assertions.assertThat(actualUserEntity.getEmail()).isEqualTo(expectedUserEntity.getEmail());
        Assertions.assertThat(actualUserEntity.getName()).isEqualTo(expectedUserEntity.getName());
        Assertions.assertThat(actualUserEntity.getRole()).isEqualTo(expectedUserEntity.getRole());
        Assertions.assertThat(actualUserEntity.getAuthority()).isEqualTo(expectedUserEntity.getAuthority());
        Assertions.assertThat(actualUserEntity.getPhone()).isEqualTo(expectedUserEntity.getPhone());
        Assertions.assertThat(actualUserEntity.getAddress().getZipCode()).isEqualTo(expectedUserEntity.getAddress().getZipCode());
        Assertions.assertThat(actualUserEntity.getAddress().getAddress()).isEqualTo(expectedUserEntity.getAddress().getAddress());
        Assertions.assertThat(actualUserEntity.getAddress().getDetailAddress()).isEqualTo(expectedUserEntity.getAddress().getDetailAddress());
        Assertions.assertThat(actualUserEntity.getSignupMethod()).isEqualTo(expectedUserEntity.getSignupMethod());
    }
}
