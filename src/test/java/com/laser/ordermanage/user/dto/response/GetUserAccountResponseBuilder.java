package com.laser.ordermanage.user.dto.response;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;

public class GetUserAccountResponseBuilder {
    public static GetUserAccountResponse build() {
        UserEntity user = UserEntityBuilder.build();

        return GetUserAccountResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .zipCode(user.getAddress().getZipCode())
                .address(user.getAddress().getAddress())
                .detailAddress(user.getAddress().getDetailAddress())
                .emailNotification(user.getEmailNotification())
                .signupMethod(user.getSignupMethod())
                .build();
    }
}
