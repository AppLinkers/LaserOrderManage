package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.List;

public record GetOrderResponse(
        Long id,
        String name,
        Boolean isUrgent,
        String stage,
        List<String> manufacturingList,
        List<String> postProcessingList,
        List<GetDrawingResponse> drawingList,
        String request,
        GetOrderDeliveryAddressResponse deliveryAddress,
        LocalDateTime createdAt
) {
    @QueryProjection
    public GetOrderResponse(Long id, String name, Boolean isUrgent, Stage stage, OrderManufacturing manufacturing, OrderPostProcessing postProcessing, List<GetDrawingResponse> drawingList, String request, GetOrderDeliveryAddressResponse deliveryAddress, LocalDateTime createdAt) {
        this(
                id,
                name,
                isUrgent,
                stage.getValue(),
                manufacturing.toValueList(),
                postProcessing.toValueList(),
                drawingList,
                request,
                deliveryAddress,
                createdAt
        );
    }
}
