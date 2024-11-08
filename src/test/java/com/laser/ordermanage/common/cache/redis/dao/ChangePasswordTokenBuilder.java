package com.laser.ordermanage.common.cache.redis.dao;

public class ChangePasswordTokenBuilder {
    public static ChangePasswordToken build() {
        return ChangePasswordToken.builder()
                .email("user@gmail.com")
                .changePasswordToken("change-password-token")
                .build();
    }

}
