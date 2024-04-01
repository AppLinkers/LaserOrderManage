package com.laser.ordermanage.user.dto.request;

public class VerifyEmailRequestBuilder {

    public static VerifyEmailRequest build() {
        return VerifyEmailRequest.builder()
                .email("new-user@gmail.com")
                .code("123456")
                .build();
    }

    public static VerifyEmailRequest duplicatedUserBuild() {
        return VerifyEmailRequest.builder()
                .email("user1@gmail.com")
                .code("123456")
                .build();
    }

    public static VerifyEmailRequest nullEmailBuild() {
        return VerifyEmailRequest.builder()
                .email(null)
                .code("123456")
                .build();
    }

    public static VerifyEmailRequest invalidEmailBuild() {
        return VerifyEmailRequest.builder()
                .email(".user.name@domain.com")
                .code("123456")
                .build();
    }

    public static VerifyEmailRequest nullCodeBuild() {
        return VerifyEmailRequest.builder()
                .email("new-user@gmail.com")
                .code(null)
                .build();
    }

    public static VerifyEmailRequest invalidCodeBuild() {
        return VerifyEmailRequest.builder()
                .email("new-user@gmail.com")
                .code("invalid-code")
                .build();
    }

    public static VerifyEmailRequest unknownUserBuild() {
        return VerifyEmailRequest.builder()
                .email("unknown-user@gmail.com")
                .code("123456")
                .build();
    }
}
