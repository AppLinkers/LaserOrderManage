package com.laser.ordermanage.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class GetUserEmailResponse {

    private final String name;

    private final String email;

    @QueryProjection
    public GetUserEmailResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
