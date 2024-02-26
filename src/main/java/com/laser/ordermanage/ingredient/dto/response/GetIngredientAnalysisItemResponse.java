package com.laser.ordermanage.ingredient.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetIngredientAnalysisItemResponse(
        String item,
        List<Number> data
) {
}
