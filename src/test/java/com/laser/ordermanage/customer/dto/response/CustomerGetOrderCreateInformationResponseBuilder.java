package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.domain.type.Ingredient;
import com.laser.ordermanage.order.dto.response.GetDrawingResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDeliveryAddressResponse;

import java.util.ArrayList;
import java.util.List;

public class CustomerGetOrderCreateInformationResponseBuilder {
    public static CustomerGetOrderCreateInformationResponse build() {
        OrderManufacturing manufacturing = OrderManufacturing.ofRequest(List.of("bending"));
        OrderPostProcessing postProcessing = OrderPostProcessing.ofRequest(new ArrayList<>());
        GetDrawingResponse drawing = new GetDrawingResponse(1L, "test.dwg", 140801L, DrawingFileType.DWG, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", 1, Ingredient.SS400, 10);
        GetOrderDeliveryAddressResponse deliveryAddress = new GetOrderDeliveryAddressResponse(1L, "고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");

        return new CustomerGetOrderCreateInformationResponse(1L, "거래 1 이름", manufacturing, postProcessing, List.of(drawing), null, deliveryAddress);
    }
}
