package com.laser.ordermanage.user.dto.request;

public class ChangePasswordRequestBuilder {

    public static ChangePasswordRequest build() {
        return ChangePasswordRequest.builder()
                .password("new-user1-password")
                .build();
    }
}
