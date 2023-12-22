package com.laser.ordermanage.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetPasswordResponse {

    private final String name;

    private final String password;

    @Builder
    public GetPasswordResponse(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
