package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateOrderPurchaseOrderRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "purchase_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PurchaseOrder extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "inspection_period", nullable = false)
    private LocalDate inspectionPeriod;

    @Column(name = "inspection_condition", nullable = false)
    private String inspectionCondition;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Builder
    public PurchaseOrder(LocalDate inspectionPeriod, String inspectionCondition, LocalDate paymentDate) {
        this.inspectionPeriod = inspectionPeriod;
        this.inspectionCondition = inspectionCondition;
        this.paymentDate = paymentDate;
    }

    public void updatePurchaseOrderProperties(CustomerCreateOrUpdateOrderPurchaseOrderRequest request) {
        this.inspectionPeriod = request.getInspectionPeriod();
        this.inspectionCondition = request.getInspectionCondition();
        this.paymentDate = request.getPaymentDate();
    }
}
