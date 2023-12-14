package com.laser.ordermanage.factory.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FactoryUpdateOrderIsUrgentRequest {

    @NotNull(message = "거래 긴급 유무는 필수 사항입니다.")
    private Boolean isUrgent;
}
