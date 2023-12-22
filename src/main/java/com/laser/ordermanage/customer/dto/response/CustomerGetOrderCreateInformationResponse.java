package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.dto.response.GetDrawingResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDeliveryAddressResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class CustomerGetOrderCreateInformationResponse {

    private final Long id;

    private final String name;

    private final List<String> manufacturingList;

    private final List<String> postProcessingList;

    private final List<GetDrawingResponse> drawingList;

    private final String request;

    private final GetOrderDeliveryAddressResponse deliveryAddress;

    @QueryProjection
    public CustomerGetOrderCreateInformationResponse(Long id, String name, OrderManufacturing manufacturing, OrderPostProcessing postProcessing, List<GetDrawingResponse> drawingList, String request, GetOrderDeliveryAddressResponse deliveryAddress) {
        this.id = id;
        this.name = name;
        this.manufacturingList = manufacturing.toValueList();
        this.postProcessingList = postProcessing.toValueList();
        this.drawingList = drawingList;
        this.request = request;
        this.deliveryAddress = deliveryAddress;
    }
}
