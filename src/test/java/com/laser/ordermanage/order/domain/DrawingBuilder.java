package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.domain.type.Ingredient;

public class DrawingBuilder {
    public static Drawing build() {
        Order order = OrderBuilder.build();

        File<DrawingFileType> file = File.<DrawingFileType>builder()
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
}
