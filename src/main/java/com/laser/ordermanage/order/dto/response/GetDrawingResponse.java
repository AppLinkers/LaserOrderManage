package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.domain.type.Ingredient;
import com.querydsl.core.annotations.QueryProjection;

public record GetDrawingResponse (
        Long id,
        String fileName,
        Long fileSize,
        String fileType,
        String fileUrl,
        String thumbnailUrl,
        Integer count,
        String ingredient,
        Integer thickness
) {

    @QueryProjection
    public GetDrawingResponse(Long id, String fileName, Long fileSize, Object fileType, String fileUrl, String thumbnailUrl, Integer count, Ingredient ingredient, Integer thickness) {
        this(
                id,
                fileName,
                fileSize,
                ((DrawingFileType) fileType).getExtension(),
                fileUrl,
                thumbnailUrl,
                count,
                ingredient.getValue(),
                thickness
        );
    }
}
