package com.laser.ordermanage.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record GetUserEmailResponse(
        String name,
        String email
) {
    @QueryProjection
    public GetUserEmailResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
