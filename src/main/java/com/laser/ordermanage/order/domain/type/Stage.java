package com.laser.ordermanage.order.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum Stage {
    NEW("견적 대기", "new"),
    QUOTE_APPROVAL("견적 승인", "quote-approval"),
    IN_PRODUCTION("제작 중", "in-production"),
    PRODUCTION_COMPLETED("제작 완료", "production-completed"),
    COMPLETED("거래 완료", "completed");

    @Getter
    private final String value;

    @Getter
    private final String request;

    private static final Map<String, Stage> stageMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(Stage::getRequest, Function.identity())));

    public static Stage ofRequest(String request) {
        return Optional.ofNullable(stageMap.get(request)).orElseThrow(() -> new CustomCommonException(ErrorCode.INVALID_PARAMETER, "stage"));
    }

}
