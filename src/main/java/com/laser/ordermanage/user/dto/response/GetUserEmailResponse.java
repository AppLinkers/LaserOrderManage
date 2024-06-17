package com.laser.ordermanage.user.dto.response;

import com.laser.ordermanage.user.domain.type.SignupMethod;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record GetUserEmailResponse(
        String name,
        SignupMethod signupMethod,
        String email
) {
    @QueryProjection
    public GetUserEmailResponse(String name, SignupMethod signupMethod, String email) {
        this.name = name;
        this.signupMethod = signupMethod;
        this.email = email;
    }
}
