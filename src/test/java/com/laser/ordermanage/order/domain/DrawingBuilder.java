package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.FileEntity;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.domain.type.Ingredient;
import org.assertj.core.api.Assertions;

public class DrawingBuilder {
    public static Drawing build() {
        Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        FileEntity<DrawingFileType> file = FileEntity.<DrawingFileType>builder()
                .name("test.dwg")
                .size(140801L)
                .type(DrawingFileType.DWG)
                .url("https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg")
                .build();

        return Drawing.builder()
                .order(order)
                .file(file)
                .thumbnailUrl("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png")
                .count(1)
                .ingredient(Ingredient.SS400.getValue())
                .thickness(10)
                .build();
    }

    public static void assertDrawing(Drawing actualDrawing, Drawing expectedDrawing) {
        OrderBuilder.assertOrder(actualDrawing.getOrder(), expectedDrawing.getOrder());
        Assertions.assertThat(actualDrawing.getFile().getName()).isEqualTo(expectedDrawing.getFile().getName());
        Assertions.assertThat(actualDrawing.getFile().getSize()).isEqualTo(expectedDrawing.getFile().getSize());
        Assertions.assertThat(actualDrawing.getFile().getType()).isEqualTo(expectedDrawing.getFile().getType());
        Assertions.assertThat(actualDrawing.getFile().getUrl()).isEqualTo(expectedDrawing.getFile().getUrl());
        Assertions.assertThat(actualDrawing.getThumbnailUrl()).isEqualTo(expectedDrawing.getThumbnailUrl());
        Assertions.assertThat(actualDrawing.getCount()).isEqualTo(expectedDrawing.getCount());
        Assertions.assertThat(actualDrawing.getIngredient()).isEqualTo(expectedDrawing.getIngredient());
        Assertions.assertThat(actualDrawing.getThickness()).isEqualTo(expectedDrawing.getThickness());
    }
}
