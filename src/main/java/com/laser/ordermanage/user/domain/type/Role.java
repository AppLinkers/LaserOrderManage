package com.laser.ordermanage.user.domain.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ROLE_USER("사용자"),
    ROLE_FACTORY("공장"),
    ROLE_CUSTOMER("고객");

    private final String value;

}
