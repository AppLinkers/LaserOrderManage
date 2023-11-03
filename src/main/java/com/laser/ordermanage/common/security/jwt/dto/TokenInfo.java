package com.laser.ordermanage.common.security.jwt.dto;

import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
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

    public TokenInfoResponse toTokenInfoResponse() {
        return TokenInfoResponse.builder()
                .role(role)
                .grantType(grantType)
                .accessToken(accessToken)
                .accessTokenExpirationTime(accessTokenExpirationTime)
                .build();
    }
}
