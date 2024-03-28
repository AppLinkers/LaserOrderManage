package com.laser.ordermanage.order.unit.domain;

import com.laser.ordermanage.customer.dto.request.CustomerUpdateDrawingRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateDrawingRequestBuilder;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.DrawingBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DrawingUnitTest {

    @Test
    public void updateProperties() {
        // given
        final Drawing drawing = DrawingBuilder.build();
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        drawing.updateProperties(request);

        // then
        Assertions.assertThat(drawing.getCount()).isEqualTo(request.count());
        Assertions.assertThat(drawing.getIngredient().getValue()).isEqualTo(request.ingredient());
        Assertions.assertThat(drawing.getThickness()).isEqualTo(request.thickness());
    }

    public static void assertDrawing(Drawing actualDrawing, Drawing expectedDrawing) {
        OrderUnitTest.assertOrder(actualDrawing.getOrder(), expectedDrawing.getOrder());
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
