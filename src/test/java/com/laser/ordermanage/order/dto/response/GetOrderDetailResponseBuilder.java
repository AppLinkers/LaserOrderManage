package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.customer.dto.response.GetCustomerResponse;
import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.domain.type.Ingredient;
import com.laser.ordermanage.order.domain.type.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GetOrderDetailResponseBuilder {

    public static GetOrderDetailResponse build() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        GetCustomerResponse customer = GetCustomerResponse.builder()
                .id(1L)
                .name("고객 이름 1")
                .company("고객 회사 이름 1")
                .phone("01022221111")
                .email("user1@gmail.com")
                .build();

        OrderManufacturing orderManufacturing = OrderManufacturing.ofRequest(List.of("bending"));
        OrderPostProcessing orderPostProcessing = OrderPostProcessing.ofRequest(new ArrayList<>());

        GetDrawingResponse drawing = new GetDrawingResponse(1L, "test.dwg", 140801L, DrawingFileType.DWG, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", 1, Ingredient.SS400, 10);
        GetOrderDeliveryAddressResponse orderDeliveryAddress = new GetOrderDeliveryAddressResponse(1L, "고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
        LocalDateTime createdAtOfOrder = LocalDateTime.parse("2023-10-12 10:20:30", formatter);

        GetOrderResponse order = new GetOrderResponse(1L, "거래 1 이름", Boolean.FALSE, Stage.COMPLETED, orderManufacturing, orderPostProcessing, List.of(drawing), null, orderDeliveryAddress, createdAtOfOrder);

        LocalDate deliveryDate = LocalDate.parse("2023-10-19", DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime createdAtOfQuotation = LocalDateTime.parse("2023-10-14 00:00:00", formatter);
        GetQuotationResponse quotation = new GetQuotationResponse(1L, "quotation.xlsx", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/quotation.xlsx", 100000000L, deliveryDate, createdAtOfQuotation);

        LocalDate inspectionPeriod = LocalDate.parse("2023-10-20", DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate paymentDate = LocalDate.parse("2023-10-20", DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime createdAtOfPurchaseOrder = LocalDateTime.parse("2023-10-17 00:00:00", formatter);
        GetPurchaseOrderResponse purchaseOrder = new GetPurchaseOrderResponse(1L, "purchase-order.png", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/purchase-order/purchase-order.png", inspectionPeriod, "검수 조건 1", paymentDate, createdAtOfPurchaseOrder);

        GetAcquirerResponse acquirer = new GetAcquirerResponse(1L, "인수자 1 이름", "01012121212", "signature.png", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/acquirer-signature/signature.png");
        return new GetOrderDetailResponse(customer, order, quotation, purchaseOrder, acquirer);
    }

}
