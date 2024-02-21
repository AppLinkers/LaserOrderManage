package com.laser.ordermanage.ingredient.dto.response;

import java.util.List;

public record GetIngredientAnalysisItemResponse(
        String item,
        List<Integer> data
) {
}
