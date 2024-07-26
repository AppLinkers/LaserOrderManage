package com.laser.ordermanage.customer.dto.request;

import java.util.List;

public class CustomerCreateOrderRequestBuilder {

    public static CustomerCreateOrderRequest build() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullNameBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest(null, List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest emptyNameBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidNameBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름".repeat(5), List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest emptyDrawingListBuild() {
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullDrawingListBuild() {
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), null, "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullCountDrawingBuild() {
        CustomerCreateDrawingRequest nullCountDrawing = CustomerCreateDrawingRequestBuilder.nullCountBuild();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(nullCountDrawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidCountDrawingBuild() {
        CustomerCreateDrawingRequest invalidCountDrawing = CustomerCreateDrawingRequestBuilder.invalidCountBuild();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(invalidCountDrawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullIngredientDrawingBuild() {
        CustomerCreateDrawingRequest nullIngredientDrawing = CustomerCreateDrawingRequestBuilder.nullIngredientBuild();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(nullIngredientDrawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest emptyIngredientDrawingBuild() {
        CustomerCreateDrawingRequest emptyIngredientDrawing = CustomerCreateDrawingRequestBuilder.emptyIngredientBuild();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(emptyIngredientDrawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullThicknessDrawingBuild() {
        CustomerCreateDrawingRequest nullThicknessDrawing = CustomerCreateDrawingRequestBuilder.nullThicknessBuild();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(nullThicknessDrawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidThicknessDrawingBuild() {
        CustomerCreateDrawingRequest invalidThicknessDrawing = CustomerCreateDrawingRequestBuilder.invalidThicknessBuild();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(invalidThicknessDrawing), "거래 요청 사항", deliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullNameDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest nullNameDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.nullNameBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", nullNameDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest emptyNameDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest emptyNameDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.emptyNameBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", emptyNameDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidNameDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest invalidNameDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.invalidNameBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", invalidNameDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullZipCodeDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest nullZipCodeDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.nullZipCodeBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", nullZipCodeDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidZipCodeDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest invalidZipCodeDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.invalidZipCodeBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", invalidZipCodeDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullAddressDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest nullAddressDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.nullAddressBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", nullAddressDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest emptyAddressDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest emptyAddressDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.emptyAddressBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", emptyAddressDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidDetailAddressDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest invalidDetailAddressDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.invalidDetailAddressBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", invalidDetailAddressDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullReceiverDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest nullReceiverDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.nullReceiverBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", nullReceiverDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest emptyReceiverDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest emptyReceiverDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.emptyReceiverBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", emptyReceiverDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidReceiverDeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest invalidReceiverDeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.invalidReceiverBuild();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", invalidReceiverDeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullPhone1DeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest nullPhone1DeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.nullPhone1Build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", nullPhone1DeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidPhone1DeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest invalidPhone1DeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.invalidPhone1Build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", invalidPhone1DeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest invalidPhone2DeliveryAddressBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest invalidPhone2DeliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.invalidPhone2Build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", invalidPhone2DeliveryAddress, Boolean.FALSE);
    }

    public static CustomerCreateOrderRequest nullNewIssueBuild() {
        CustomerCreateDrawingRequest drawing = CustomerCreateDrawingRequestBuilder.build();
        CustomerCreateOrderDeliveryAddressRequest deliveryAddress = CustomerCreateOrderDeliveryAddressRequestBuilder.build();
        return new CustomerCreateOrderRequest("거래 이름", List.of("laser-cutting", "bending"), List.of("painting", "plating"), List.of(drawing), "거래 요청 사항", deliveryAddress, null);
    }
}
