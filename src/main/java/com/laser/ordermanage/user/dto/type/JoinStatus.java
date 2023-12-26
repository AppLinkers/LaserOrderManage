package com.laser.ordermanage.user.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum JoinStatus {

    IMPOSSIBLE("001"),
    POSSIBLE("002"),
    COMPLETED("003");

    @Getter
    private final String code;

    public static boolean isPossible(String code) {
        return code.equals(JoinStatus.POSSIBLE.getCode());
    }

}
