package com.laser.ordermanage.customer.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.PurchaseOrder;
import com.laser.ordermanage.order.domain.type.PurchaseOrderFileType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CustomerCreateOrUpdateOrderPurchaseOrderRequest (

    @NotNull(message = "검수 기간은 필수 입력값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate inspectionPeriod,

    @NotEmpty(message = "검수 조건은 필수 입력값입니다.")
    String inspectionCondition,

    @NotNull(message = "지급 일자는 필수 입력값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate paymentDate

) {
    public boolean isValidInspectionPeriod(Order order) {
        return order.getQuotation().getDeliveryDate().isAfter(inspectionPeriod);
    }

    public boolean isValidPaymentDate(Order order) {
        return order.getQuotation().getDeliveryDate().isAfter(paymentDate);
    }

    public PurchaseOrder toEntity(File<PurchaseOrderFileType> purchaseOrderFile) {
        return PurchaseOrder.builder()
                .inspectionPeriod(inspectionPeriod)
                .inspectionCondition(inspectionCondition)
                .paymentDate(paymentDate)
                .file(purchaseOrderFile)
                .build();
    }
}
