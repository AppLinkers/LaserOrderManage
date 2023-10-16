package com.laser.ordermanage.order.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum Stage {
    NEW("견적 대기", "new"),
    QUOTE_APPROVAL("견적 승인", "quote-approval"),
    IN_PRODUCTION("제작 중", "in-production"),
    SHIPPING("배송 중", "shipping"),
    COMPLETED("거래 완료", "completed");

    @Getter
    private final String value;

    @Getter
    private final String request;

    public static Stage ofRequest(String request) {
        return Arrays.stream(Stage.values())
                .filter(v -> v.getRequest().equals(request))
                .findAny()
                .orElseThrow(() -> new CustomCommonException(ErrorCode.INVALID_STAGE_PARAMS));
    }
}
