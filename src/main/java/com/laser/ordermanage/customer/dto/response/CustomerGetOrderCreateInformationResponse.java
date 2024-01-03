package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.dto.response.GetDrawingResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDeliveryAddressResponse;
import com.querydsl.core.annotations.QueryProjection;

import java.util.List;

public record CustomerGetOrderCreateInformationResponse(
        Long id,
        String name,
        List<String> manufacturingList,
        List<String> postProcessingList,
        List<GetDrawingResponse> drawingList,
        String request,
        GetOrderDeliveryAddressResponse deliveryAddress
) {
    @QueryProjection
    public CustomerGetOrderCreateInformationResponse(Long id, String name, OrderManufacturing manufacturing, OrderPostProcessing postProcessing, List<GetDrawingResponse> drawingList, String request, GetOrderDeliveryAddressResponse deliveryAddress) {
        this(
                id,
                name,
                manufacturing.toValueList(),
                postProcessing.toValueList(),
                drawingList,
                request,
                deliveryAddress
        );
    }
}
