package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.constraints.NotNull;

public record CustomerUpdateOrderDeliveryAddressRequest (

    @NotNull(message = "주소지 선택은 필수 사항입니다.")
    Long deliveryAddressId

) {}
