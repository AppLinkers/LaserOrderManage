package com.laser.ordermanage.ingredient.domain.type;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum IngredientStockType {
    ALL("all"),
    INCOMING("incoming"),
    PRODUCTION("production"),
    STOCK("stock"),
    OPTIMAL("optimal");

    @Getter
    private final String request;

    private static final Map<String, IngredientStockType> ingredientStockTypeMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(IngredientStockType::getRequest, Function.identity())));

    public static List<String> ofRequest(List<String> requestList) {
        if (requestList.contains(ALL.request)) {
            return ingredientStockTypeMap.values().stream().filter((stockType)-> !stockType.equals(ALL)).map(value -> value.request).toList();
        }

        return requestList.stream()
                .map(
                        request -> Optional.ofNullable(ingredientStockTypeMap.get(request).request)
                                .orElseThrow(() -> new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "stock-item 파라미터가 올바르지 않습니다.")))
                .collect(Collectors.toList());
    }
}
