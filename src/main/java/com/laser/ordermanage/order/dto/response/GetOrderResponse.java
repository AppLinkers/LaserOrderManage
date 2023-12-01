package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.customer.dto.response.GetDeliveryAddressResponse;
import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetOrderResponse {

    private final Long id;

    private final String name;

    private final Boolean isUrgent;

    private final String stage;

    private final List<String> manufacturingList;

    private final List<String> postProcessingList;

    private final List<GetDrawingResponse> drawingList;

    private final String request;

    private final GetDeliveryAddressResponse deliveryAddress;

    private final LocalDateTime createdAt;

    @QueryProjection
    public GetOrderResponse(Long id, String name, Boolean isUrgent, Stage stage, OrderManufacturing manufacturing, OrderPostProcessing postProcessing, List<GetDrawingResponse> drawingList, String request, GetDeliveryAddressResponse deliveryAddress, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.isUrgent = isUrgent;
        this.stage = stage.getValue();
        this.manufacturingList = manufacturing.toValueList();
        this.postProcessingList = postProcessing.toValueList();
        this.drawingList = drawingList;
        this.request = request;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = createdAt;
    }
}
