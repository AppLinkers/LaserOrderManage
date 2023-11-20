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
public enum Ingredient {
    SS_330("SS 330");

    @Getter
    private final String name;

    private static final Map<String, Ingredient> extensionMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(Ingredient::getName, Function.identity())));

    public static Ingredient ofName(String request) {
        return Optional.ofNullable(extensionMap.get(request)).orElseThrow(() -> new CustomCommonException(ErrorCode.INVALID_INGREDIENT));
    }
}
