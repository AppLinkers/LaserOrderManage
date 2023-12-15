package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class CustomerCreateOrUpdateOrderPurchaseOrderRequest {

    @NotNull(message = "검수 기간은 필수 입력값입니다.")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate inspectionPeriod;

    @NotEmpty(message = "검수 조건은 필수 입력값입니다.")
    private String inspectionCondition;

    @NotNull(message = "지급 일자는 필수 입력값입니다.")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate paymentDate;
}
