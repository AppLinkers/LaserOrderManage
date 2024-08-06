package com.laser.ordermanage.user.dto.request;

public class LoginKakaoRequestBuilder {
    public static LoginKakaoRequest build() {
        return LoginKakaoRequest.builder()
                .kakaoAccessToken("kakao-access-token")
                .build();
    }
}
