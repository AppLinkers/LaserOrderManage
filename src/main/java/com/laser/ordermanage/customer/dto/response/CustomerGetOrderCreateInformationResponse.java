package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class CustomerGetOrderCreateInformationResponse {

    private final Long id;

    private final String name;

    private final List<String> manufacturingList;

    private final List<String> postProcessingList;

    @Setter
    private List<CustomerGetDrawingResponse> drawingList;

    private final String request;

    private final CustomerGetDeliveryAddressResponse deliveryAddress;

    @QueryProjection
    public CustomerGetOrderCreateInformationResponse(Long id, String name, OrderManufacturing manufacturing, OrderPostProcessing postProcessing, String request, CustomerGetDeliveryAddressResponse deliveryAddress) {
        this.id = id;
        this.name = name;
        this.manufacturingList = manufacturing.toValueList();
        this.postProcessingList = postProcessing.toValueList();
        this.request = request;
        this.deliveryAddress = deliveryAddress;
    }
}
