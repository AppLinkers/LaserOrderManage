package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CustomerUpdateOrderDeliveryAddressRequest {

    @NotNull(message = "주소지 선택은 필수 사항입니다.")
    private Long deliveryAddressId;
}
