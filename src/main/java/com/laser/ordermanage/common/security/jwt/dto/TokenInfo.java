package com.laser.ordermanage.common.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenInfo {

    private String role;
    private String grantType;
    private String accessToken;
    private Long accessTokenExpirationTime;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
}
