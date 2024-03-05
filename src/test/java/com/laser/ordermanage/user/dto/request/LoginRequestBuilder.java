package com.laser.ordermanage.user.dto.request;

public class LoginRequestBuilder {

    public static LoginRequest build() {
        return LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();
    }

    public static LoginRequest invalidBuild() {
        return LoginRequest.builder()
                .email("invalid-user@gmail.com")
                .password("invalid-user1-password")
                .build();
    }

    public static LoginRequest newPasswordBuild() {
        return LoginRequest.builder()
                .email("user1@gmail.com")
                .password("new-user1-password")
                .build();
    }

}
