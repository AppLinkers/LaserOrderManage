package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.order.domain.type.QuotationFileType;

import java.time.LocalDate;

public class QuotationBuilder {
    public static Quotation build() {
        File<QuotationFileType> file = File.<QuotationFileType>builder()
                .name("quotation.xlsx")
                .size(306480L)
                .type(QuotationFileType.PDF)
                .url("https://ordermanage.s3.ap-northeast-2.amazonaws.com/quotation.xlsx")
                .build();

        return Quotation.builder()
                .totalCost(100000000L)
                .file(file)
                .deliveryDate(LocalDate.of(2023, 10, 19))
                .build();
    }
}
