package com.laser.ordermanage.factory.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FactoryCreateOrUpdateOrderQuotationRequest (

    @NotNull(message = "총 견적 비용은 필수 입력값입니다.")
    Long totalCost,

    @NotNull(message = "납기일은 필수 입력값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate deliveryDate

) {}
