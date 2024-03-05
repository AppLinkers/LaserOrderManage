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

    public static RequestChangePasswordRequest nullEmailBuild() {
        return RequestChangePasswordRequest.builder()
                .email(null)
                .baseUrl("https://www.kumoh.org/edit-password")
                .build();
    }

    public static RequestChangePasswordRequest invalidEmailBuild() {
        return RequestChangePasswordRequest.builder()
                .email(".user.name@domain.com")
                .baseUrl("https://www.kumoh.org/edit-password")
                .build();
    }

    public static RequestChangePasswordRequest nullBaseURLBuild() {
        return RequestChangePasswordRequest.builder()
                .email("user1@gmail.com")
                .baseUrl(null)
                .build();
    }

    public static RequestChangePasswordRequest invalidBaseURLBuild() {
        return RequestChangePasswordRequest.builder()
                .email("user1@gmail.com")
                .baseUrl("www.invalid.url.com")
                .build();
    }
}
