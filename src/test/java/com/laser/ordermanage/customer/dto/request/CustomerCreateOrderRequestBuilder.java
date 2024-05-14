package com.laser.ordermanage.customer.dto.request;

import java.util.List;

public class CustomerCreateOrderRequestBuilder {

    public static CustomerCreateOrderRequest build() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

}
