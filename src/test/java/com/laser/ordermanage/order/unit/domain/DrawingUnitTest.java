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
}
