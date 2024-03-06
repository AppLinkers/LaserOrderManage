package com.laser.ordermanage.user.dto.request;

public class LoginRequestBuilder {

    public static LoginRequest build() {
        return LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();
    }

    public static LoginRequest nullEmailBuild() {
        return LoginRequest.builder()
                .password("user1-password")
                .build();
    }

    public static LoginRequest invalidEmailBuild() {
        return LoginRequest.builder()
                .email("invalid-email")
                .password("user1-password")
                .build();
    }

    public static LoginRequest nullPasswordBuild() {
        return LoginRequest.builder()
                .email("user1@gmail.com")
                .build();
    }

    public static LoginRequest invalidPasswordBuild() {
        return LoginRequest.builder()
                .email("user1@gmail.com")
                .password("invalid-password")
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
