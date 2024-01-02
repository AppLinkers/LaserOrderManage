package com.laser.ordermanage.user.dto.response;

import lombok.Builder;

@Builder
public record TokenInfoResponse(
        String role,
        String grantType,
        String accessToken,
        Long accessTokenExpirationTime,
        String refreshToken,
        Long refreshTokenExpirationTime
) {
}
