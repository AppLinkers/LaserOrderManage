package com.laser.ordermanage.user.dto.request;

public class RequestChangePasswordRequestBuilder {

    public static RequestChangePasswordRequest build() {
        return RequestChangePasswordRequest.builder()
                .email("user1@gmail.com")
                .baseUrl("https://www.kumoh.org/edit-password")
                .build();
    }

    public static RequestChangePasswordRequest unknownUserBuild() {
        return RequestChangePasswordRequest.builder()
                .email("unknwon-user@gmail.com")
                .baseUrl("https://www.kumoh.org/edit-password")
                .build();
    }
}
