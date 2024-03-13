package com.laser.ordermanage.user.dto.response;

import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.user.domain.type.Authority;
import com.laser.ordermanage.user.domain.type.Role;

import java.util.List;

public class TokenInfoResponseBuilder {

    public static TokenInfoResponse build() {
        return TokenInfoResponse.builder()
                .authorityList(List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()))
                .grantType("Bearer")
                .accessToken("accessToken")
                .accessTokenExpirationTime(ExpireTime.ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken("refreshToken")
                .refreshTokenExpirationTime(ExpireTime.REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

}
