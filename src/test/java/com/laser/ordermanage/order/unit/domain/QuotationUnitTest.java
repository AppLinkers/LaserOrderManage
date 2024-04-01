package com.laser.ordermanage.order.unit.domain;

import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderQuotationRequest;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderQuotationRequestBuilder;
import com.laser.ordermanage.order.domain.Quotation;
import com.laser.ordermanage.order.domain.QuotationBuilder;
import com.laser.ordermanage.order.domain.type.QuotationFileType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuotationUnitTest {

    @Test
    public void updateFile() {
        // given
        final Quotation quotation = QuotationBuilder.build();
        final File<QuotationFileType> file = File.<QuotationFileType>builder()
                .name("new_quotation.pdf")
                .size(153240L)
                .type(QuotationFileType.JPG)
                .url("https://ordermanage.s3.ap-northeast-2.amazonaws.com/new_quotation.pdf")
                .build();

        // when
        quotation.updateFile(file);

        // then
        Assertions.assertThat(quotation.getFile()).isEqualTo(file);
    }

    @Test
    public void updateProperties() {
        // given
        final Quotation quotation = QuotationBuilder.build();
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        quotation.updateProperties(request);

        // then
        Assertions.assertThat(quotation.getTotalCost()).isEqualTo(request.totalCost());
        Assertions.assertThat(quotation.getDeliveryDate()).isEqualTo(request.deliveryDate());
    }
}
