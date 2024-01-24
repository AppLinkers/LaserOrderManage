package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateOrderPurchaseOrderRequest;
import com.laser.ordermanage.order.domain.type.PurchaseOrderFileType;
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

    @Embedded
    private File<PurchaseOrderFileType> file;

    @Builder
    public PurchaseOrder(LocalDate inspectionPeriod, String inspectionCondition, LocalDate paymentDate, File<PurchaseOrderFileType> file) {
        this.inspectionPeriod = inspectionPeriod;
        this.inspectionCondition = inspectionCondition;
        this.paymentDate = paymentDate;
        this.file = file;
    }

    public void updateFile(File<PurchaseOrderFileType> file) {
        this.file = file;
    }

    public void updateProperties(CustomerCreateOrUpdateOrderPurchaseOrderRequest request) {
        this.inspectionPeriod = request.inspectionPeriod();
        this.inspectionCondition = request.inspectionCondition();
        this.paymentDate = request.paymentDate();
    }
}
