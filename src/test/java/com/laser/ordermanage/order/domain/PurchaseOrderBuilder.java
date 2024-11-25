package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.FileEntity;
import com.laser.ordermanage.order.domain.type.PurchaseOrderFileType;

import java.time.LocalDate;

public class PurchaseOrderBuilder {
    public static PurchaseOrder build() {
        FileEntity<PurchaseOrderFileType> file = FileEntity.<PurchaseOrderFileType>builder()
                .name("purchase-order.png")
                .size(3608L)
                .type(PurchaseOrderFileType.PNG)
                .url("https://ordermanage.s3.ap-northeast-2.amazonaws.com/purchase-order/purchase-order.png")
                .build();

        return PurchaseOrder.builder()
                .inspectionPeriod(LocalDate.of(2023, 10, 20))
                .inspectionCondition("검수 조건 1")
                .paymentDate(LocalDate.of(2023, 10, 20))
                .file(file)
                .build();
    }
}
