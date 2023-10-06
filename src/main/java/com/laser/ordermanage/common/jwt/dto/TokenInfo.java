package com.laser.ordermanage.common.jwt.dto;

import com.laser.ordermanage.user.dto.response.TokenInfoRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenInfo {

    private String grantType;
    private String accessToken;
    private Long accessTokenExpirationTime;
    private String refreshToken;

    public TokenInfoRes toTokenInfoRes() {
        return TokenInfoRes.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .accessTokenExpirationTime(accessTokenExpirationTime)
                .build();
    }
}
