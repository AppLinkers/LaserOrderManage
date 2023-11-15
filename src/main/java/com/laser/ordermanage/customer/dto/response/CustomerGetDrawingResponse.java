package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.type.FileType;
import com.laser.ordermanage.order.domain.type.Ingredient;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CustomerGetDrawingResponse {

    private final Long id;

    private final String fileName;

    private final Long fileSize;

    private final String fileType;

    private final String fileUrl;

    private final String thumbnailUrl;

    private final Integer count;

    private final String ingredient;

    @QueryProjection
    public CustomerGetDrawingResponse(Long id, String fileName, Long fileSize, FileType fileType, String fileUrl, String thumbnailUrl, Integer count, Ingredient ingredient) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType.getExtension();
        this.fileUrl = fileUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.count = count;
        this.ingredient = ingredient.getName();
    }
}
