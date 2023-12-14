package com.laser.ordermanage.factory.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FactoryUpdateOrderIsUrgentRequest {

    @NotNull
    private Boolean isUrgent;
}
