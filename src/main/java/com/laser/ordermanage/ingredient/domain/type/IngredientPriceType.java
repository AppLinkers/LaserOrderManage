package com.laser.ordermanage.ingredient.domain.type;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum IngredientPriceType {
    ALL("all"),
    PURCHASE("purchase"),
    SELL("sell");

    @Getter
    private final String request;

    private static final Map<String, IngredientPriceType> ingredientPriceTypeMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(IngredientPriceType::getRequest, Function.identity())));

    public static List<IngredientPriceType> ofRequest(List<String> requestList) {
        return requestList.stream()
                .map(
                        request -> Optional.ofNullable(ingredientPriceTypeMap.get(request))
                                .orElseThrow(() -> new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "price-item 파라미터가 올바르지 않습니다.")))
                .collect(Collectors.toList());
    }
}
