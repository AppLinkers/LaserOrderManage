package com.laser.ordermanage.order.unit.domain;

import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateOrderPurchaseOrderRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder;
import com.laser.ordermanage.order.domain.PurchaseOrder;
import com.laser.ordermanage.order.domain.PurchaseOrderBuilder;
import com.laser.ordermanage.order.domain.type.PurchaseOrderFileType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PurchaseOrderUnitTest {

    @Test
    public void updateFile() {
        // given
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        final File<PurchaseOrderFileType> file = File.<PurchaseOrderFileType>builder()
                .name("new_purchase_order.png")
                .size(7216L)
                .type(PurchaseOrderFileType.JPG)
                .url("https://ordermanage.s3.ap-northeast-2.amazonaws.com/purchase-order/new_purchase_order.png")
                .build();

        // when
        purchaseOrder.updateFile(file);

        // then
        Assertions.assertThat(purchaseOrder.getFile()).isEqualTo(file);
    }

    @Test
    public void updateProperties() {
        // given
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        purchaseOrder.updateProperties(request);

        // then
        Assertions.assertThat(purchaseOrder.getInspectionPeriod()).isEqualTo(request.inspectionPeriod());
        Assertions.assertThat(purchaseOrder.getInspectionCondition()).isEqualTo(request.inspectionCondition());
        Assertions.assertThat(purchaseOrder.getPaymentDate()).isEqualTo(request.paymentDate());
    }
}
