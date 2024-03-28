package com.laser.ordermanage.order.unit.domain;

import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.domain.DeliveryAddressBuilder;
import com.laser.ordermanage.order.domain.*;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.user.unit.domain.UserEntityUnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderUnitTest {

    @Test
    public void enableUpdateIsUrgent_True() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.enableUpdateIsUrgent()).isTrue();
    }

    @Test
    public void enableUpdateIsUrgent_False() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        // when & then
        Assertions.assertThat(order.enableUpdateIsUrgent()).isFalse();
    }

    @Test
    public void updateIsUrgent() {
        // given
        final Order order = OrderBuilder.build();
        final Boolean isUrgent = Boolean.TRUE;

        // when
        order.updateIsUrgent(isUrgent);

        // then
        Assertions.assertThat(order.getIsUrgent()).isEqualTo(isUrgent);
    }

    @Test
    public void enableUpdateDeliveryAddress_True() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.enableUpdateDeliveryAddress()).isTrue();
    }

    @Test
    public void enableUpdateDeliveryAddress_False() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToProductionCompleted();

        // when & then
        Assertions.assertThat(order.enableUpdateDeliveryAddress()).isFalse();
    }

    @Test
    public void updateDeliveryAddress() {
        // given
        final Order order = OrderBuilder.build();
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build2();
        final OrderDeliveryAddress expectedDeliveryAddress = OrderDeliveryAddressBuilder.build2();

        // when
        order.updateDeliveryAddress(deliveryAddress);

        // then
        OrderDeliveryAddressUnitTest.assertOrderDeliveryAddress(order.getDeliveryAddress(), expectedDeliveryAddress);
    }

    @Test
    public void enableManageDrawing_True() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.enableManageDrawing()).isTrue();
    }

    @Test
    public void enableManageDrawing_False() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToProductionCompleted();

        // when & then
        Assertions.assertThat(order.enableManageDrawing()).isFalse();
    }

    @Test
    public void hasQuotation_True() {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        // when & then
        Assertions.assertThat(order.hasQuotation()).isTrue();
    }

    @Test
    public void hasQuotation_False() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.hasQuotation()).isFalse();
    }

    @Test
    public void enableManageQuotation_True() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.enableManageQuotation()).isTrue();
    }

    @Test
    public void enableManageQuotation_False() {
        // given
        final Order order = OrderBuilder.build();
        order.approveQuotation();

        // when & then
        Assertions.assertThat(order.enableManageQuotation()).isFalse();
    }

    @Test
    public void createQuotation() {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        Assertions.assertThat(order.getQuotation()).isNull();

        // when
        order.createQuotation(quotation);

        // then
        Assertions.assertThat(order.getQuotation()).isEqualTo(quotation);
    }

    @Test
    public void enableApproveQuotation_True() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.enableApproveQuotation()).isTrue();
    }

    @Test
    public void enableApproveQuotation_False() {
        // given
        final Order order = OrderBuilder.build();
        order.approveQuotation();

        // when & then
        Assertions.assertThat(order.enableApproveQuotation()).isFalse();
    }

    @Test
    public void approveQuotation() {
        // given
        final Order order = OrderBuilder.build();
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.NEW);

        // when
        order.approveQuotation();

        // then
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.QUOTE_APPROVAL);
    }

    @Test
    public void hasPurchaseOrder_True() {
        // given
        final Order order = OrderBuilder.build();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);

        // when & then
        Assertions.assertThat(order.hasPurchaseOrder()).isTrue();
    }

    @Test
    public void hasPurchaseOrder_False() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.hasPurchaseOrder()).isFalse();
    }

    @Test
    public void enableManagePurchaseOrder_True() {
        // given
        final Order order = OrderBuilder.build();
        order.approveQuotation();

        // when & then
        Assertions.assertThat(order.enableManagePurchaseOrder()).isTrue();
    }

    @Test
    public void enableManagePurchaseOrder_False() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.enableManagePurchaseOrder()).isFalse();
    }

    @Test
    public void createPurchaseOrder() {
        // given
        final Order order = OrderBuilder.build();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        Assertions.assertThat(order.getPurchaseOrder()).isNull();

        // when
        order.createPurchaseOrder(purchaseOrder);

        // then
        Assertions.assertThat(order.getPurchaseOrder()).isEqualTo(purchaseOrder);
    }

    @Test
    public void enableApprovePurchaseOrder_True() {
        // given
        final Order order = OrderBuilder.build();
        order.approveQuotation();

        // when & then
        Assertions.assertThat(order.enableApprovePurchaseOrder()).isTrue();
    }

    @Test
    public void enableApprovePurchaseOrder_False() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.enableApprovePurchaseOrder()).isFalse();
    }

    @Test
    public void approvePurchaseOrder() {
        // given
        final Order order = OrderBuilder.build();
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.NEW);

        // when
        order.approvePurchaseOrder();

        // then
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.IN_PRODUCTION);
    }

    @Test
    public void enableChangeStageToProductionCompleted_True() {
        // given
        final Order order = OrderBuilder.build();
        order.approvePurchaseOrder();

        // when & then
        Assertions.assertThat(order.enableChangeStageToProductionCompleted()).isTrue();
    }

    @Test
    public void enableChangeStageToProductionCompleted_False() {
        // given
        final Order order = OrderBuilder.build();
        order.approveQuotation();

        // when & then
        Assertions.assertThat(order.enableChangeStageToProductionCompleted()).isFalse();
    }

    @Test
    public void changeStageToProductionCompleted() {
        // given
        final Order order = OrderBuilder.build();
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.NEW);

        // when
        order.changeStageToProductionCompleted();

        // then
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.PRODUCTION_COMPLETED);
    }

    @Test
    public void hasAcquirer_True() {
        // given
        final Order order = OrderBuilder.build();
        final Acquirer acquirer = AcquirerBuilder.build();
        order.createAcquirer(acquirer);

        // when & then
        Assertions.assertThat(order.hasAcquirer()).isTrue();
    }

    @Test
    public void hasAcquirer_False() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.hasAcquirer()).isFalse();
    }

    @Test
    public void createAcquirer() {
        // given
        final Order order = OrderBuilder.build();
        final Acquirer acquirer = AcquirerBuilder.build();
        Assertions.assertThat(order.getAcquirer()).isNull();

        // when
        order.createAcquirer(acquirer);

        // then
        Assertions.assertThat(order.getAcquirer()).isEqualTo(acquirer);
    }

    @Test
    public void enableChangeStageToCompleted_True() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToProductionCompleted();

        // when & then
        Assertions.assertThat(order.enableChangeStageToCompleted()).isTrue();
    }

    @Test
    public void enableChangeStageToCompleted_False() {
        // given
        final Order order = OrderBuilder.build();
        order.approvePurchaseOrder();

        // when & then
        Assertions.assertThat(order.enableChangeStageToCompleted()).isFalse();
    }

    @Test
    public void changeStageToCompleted() {
        // given
        final Order order = OrderBuilder.build();
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.NEW);
        Assertions.assertThat(order.getCompletedAt()).isNull();

        // when
        order.changeStageToCompleted();

        // then
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.COMPLETED);
        Assertions.assertThat(order.getCompletedAt()).isNotNull();
    }

    @Test
    public void enableDelete_True() {
        // given
        final Order order = OrderBuilder.build();
        order.approveQuotation();

        // when & then
        Assertions.assertThat(order.enableDelete()).isTrue();
    }

    @Test
    public void enableDelete_False() {
        // given
        final Order order = OrderBuilder.build();
        order.approvePurchaseOrder();

        // when & then
        Assertions.assertThat(order.enableDelete()).isFalse();
    }

    @Test
    public void delete() {
        // given
        final Order order = OrderBuilder.build();
        Assertions.assertThat(order.getCustomer()).isNotNull();
        Assertions.assertThat(order.getIsDeleted()).isFalse();

        // when
        order.delete();

        // then
        Assertions.assertThat(order.getCustomer()).isNull();
        Assertions.assertThat(order.getIsDeleted()).isTrue();
    }

    @Test
    public void hasCustomer_True() {
        // given
        final Order order = OrderBuilder.build();

        // when & then
        Assertions.assertThat(order.hasCustomer()).isTrue();
    }

    @Test
    public void hasCustomer_False() {
        // given
        final Order order = OrderBuilder.build();
        order.delete();

        // when & then
        Assertions.assertThat(order.hasCustomer()).isFalse();
    }

    public static void assertOrder(Order actualOrder, Order expectedOrder) {
        UserEntityUnitTest.assertUserEntity(actualOrder.getCustomer().getUser(), expectedOrder.getCustomer().getUser());
        Assertions.assertThat(actualOrder.getCustomer().getCompanyName()).isEqualTo(expectedOrder.getCustomer().getCompanyName());
        Assertions.assertThat(actualOrder.getCustomer().getIsNew()).isEqualTo(expectedOrder.getCustomer().getIsNew());
        OrderDeliveryAddressUnitTest.assertOrderDeliveryAddress(actualOrder.getDeliveryAddress(), expectedOrder.getDeliveryAddress());
        Assertions.assertThat(actualOrder.getName()).isEqualTo(expectedOrder.getName());
        Assertions.assertThat(actualOrder.getImgUrl()).isEqualTo(expectedOrder.getImgUrl());
        Assertions.assertThat(actualOrder.getStage()).isEqualTo(expectedOrder.getStage());
        OrderManufacturingUnitTest.assertOrderManufacturing(actualOrder.getManufacturing(), expectedOrder.getManufacturing());
        OrderPostProcessingUnitTest.assertOrderPostProcessing(actualOrder.getPostProcessing(), expectedOrder.getPostProcessing());
        Assertions.assertThat(actualOrder.getRequest()).isEqualTo(expectedOrder.getRequest());
        Assertions.assertThat(actualOrder.getIsNewIssue()).isEqualTo(expectedOrder.getIsNewIssue());
        Assertions.assertThat(actualOrder.getIsDeleted()).isEqualTo(expectedOrder.getIsDeleted());
    }
}
