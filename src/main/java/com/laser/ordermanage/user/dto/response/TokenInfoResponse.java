package com.laser.ordermanage.user.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TokenInfoResponse(
        List<String> authorityList,
        String grantType,
        String accessToken,
        Long accessTokenExpirationTime,
        String refreshToken,
        Long refreshTokenExpirationTime
) {
}
