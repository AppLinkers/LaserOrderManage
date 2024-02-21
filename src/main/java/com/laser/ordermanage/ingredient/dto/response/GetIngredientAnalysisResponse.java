package com.laser.ordermanage.ingredient.dto.response;

import com.laser.ordermanage.common.paging.ListResponse;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetIngredientAnalysisResponse(
        String timeUnit,
        LocalDate startDate,
        LocalDate endDate,
        ListResponse<GetIngredientAnalysisItemResponse> itemList
) {
}
