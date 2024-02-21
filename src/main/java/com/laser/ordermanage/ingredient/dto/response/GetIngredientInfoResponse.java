package com.laser.ordermanage.ingredient.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record GetIngredientInfoResponse(
        Long id,
        String texture,
        Double thickness
) {
    @QueryProjection
    public GetIngredientInfoResponse(Long id, String texture, Double thickness) {
        this.id = id;
        this.texture = texture;
        this.thickness = thickness;
    }
}
