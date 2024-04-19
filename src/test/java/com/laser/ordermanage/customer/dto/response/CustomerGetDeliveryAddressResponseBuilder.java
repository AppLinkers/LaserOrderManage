package com.laser.ordermanage.customer.dto.response;

import java.util.List;

public class CustomerGetDeliveryAddressResponseBuilder {
    public static List<CustomerGetDeliveryAddressResponse> buildListOfCustomer1() {
        CustomerGetDeliveryAddressResponse deliveryAddress1 = new CustomerGetDeliveryAddressResponse(1L, "고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222", Boolean.TRUE);

        CustomerGetDeliveryAddressResponse deliveryAddress2 = new CustomerGetDeliveryAddressResponse(8L, "고객 1 배송지 3", "88888", "배송지 8 기본 주소", "배송지 8 상세 주소", "배송지 8 수신자", "01088881111", "01088882222", Boolean.FALSE);

        CustomerGetDeliveryAddressResponse deliveryAddress3 = new CustomerGetDeliveryAddressResponse(2L, "고객 1 배송지 2", "22222", "배송지 2 기본 주소", "배송지 2 상세 주소", "배송지 2 수신자", "01022221111", "01022222222", Boolean.FALSE);

        return List.of(deliveryAddress1, deliveryAddress2, deliveryAddress3);
    }
}
