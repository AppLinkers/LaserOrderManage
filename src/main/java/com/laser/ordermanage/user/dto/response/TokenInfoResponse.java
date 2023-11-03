package com.laser.ordermanage.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenInfoResponse {

    private String role;
    private String grantType;
    private String accessToken;
    private Long accessTokenExpirationTime;

}
