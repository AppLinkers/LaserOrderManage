package com.laser.ordermanage.factory.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class FactoryCreateOrUpdateOrderQuotationRequest {

    @NotNull(message = "총 견적 비용은 필수 입력값입니다.")
    private Long totalCost;

    @NotNull(message = "납기일은 필수 입력값입니다.")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate deliveryDate;

}
