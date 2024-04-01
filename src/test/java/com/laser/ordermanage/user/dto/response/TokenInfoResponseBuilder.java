package com.laser.ordermanage.user.dto.response;

import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.user.domain.type.Authority;
import com.laser.ordermanage.user.domain.type.Role;
import org.assertj.core.api.Assertions;

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

    public static void assertTokenInfoResponse(TokenInfoResponse actualResponse, TokenInfoResponse expectedResponse) {
        Assertions.assertThat(actualResponse.authorityList()).hasSameElementsAs(expectedResponse.authorityList());
        Assertions.assertThat(actualResponse.grantType()).isEqualTo(expectedResponse.grantType());
        Assertions.assertThat(actualResponse.accessToken()).isNotEmpty();
        Assertions.assertThat(actualResponse.refreshToken()).isNotEmpty();
        Assertions.assertThat(actualResponse.accessTokenExpirationTime()).isEqualTo(expectedResponse.accessTokenExpirationTime());
        Assertions.assertThat(actualResponse.refreshTokenExpirationTime()).isEqualTo(expectedResponse.refreshTokenExpirationTime());
    }
}
