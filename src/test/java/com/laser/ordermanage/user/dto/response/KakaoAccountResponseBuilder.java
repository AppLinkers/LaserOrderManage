package com.laser.ordermanage.user.dto.response;

import java.time.LocalDateTime;

public class KakaoAccountResponseBuilder {

    public static KakaoAccountResponse newKakaoBuild() {
        return KakaoAccountResponse.builder()
                .id(1L)
                .connected_at(LocalDateTime.now())
                .kakao_account(KakaoAccountResponse.KakaoAccount.builder()
                        .name_needs_agreement(Boolean.FALSE)
                        .name("사용자 이름")
                        .has_email(Boolean.TRUE)
                        .email_needs_agreement(Boolean.FALSE)
                        .is_email_valid(Boolean.TRUE)
                        .email("new-kakao@gmail.com")
                        .has_phone_number(Boolean.TRUE)
                        .phone_number_needs_agreement(Boolean.FALSE)
                        .phone_number("+82 10-1111-1111").build())
                .build();
    }

    public static KakaoAccountResponse existKakaoBuild() {
        return KakaoAccountResponse.builder()
                .id(1L)
                .connected_at(LocalDateTime.now())
                .kakao_account(KakaoAccountResponse.KakaoAccount.builder()
                        .name_needs_agreement(Boolean.FALSE)
                        .name("사용자 이름")
                        .has_email(Boolean.TRUE)
                        .email_needs_agreement(Boolean.FALSE)
                        .is_email_valid(Boolean.TRUE)
                        .email("user2@gmail.com")
                        .has_phone_number(Boolean.TRUE)
                        .phone_number_needs_agreement(Boolean.FALSE)
                        .phone_number("+82 10-1111-1111").build())
                .build();
    }

    public static KakaoAccountResponse existBasicBuild() {
        return KakaoAccountResponse.builder()
                .id(1L)
                .connected_at(LocalDateTime.now())
                .kakao_account(KakaoAccountResponse.KakaoAccount.builder()
                        .name_needs_agreement(Boolean.FALSE)
                        .name("사용자 이름")
                        .has_email(Boolean.TRUE)
                        .email_needs_agreement(Boolean.FALSE)
                        .is_email_valid(Boolean.TRUE)
                        .email("user1@gmail.com")
                        .has_phone_number(Boolean.TRUE)
                        .phone_number_needs_agreement(Boolean.FALSE)
                        .phone_number("+82 10-1111-1111").build())
                .build();
    }
}
