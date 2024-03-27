package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.order.domain.type.Stage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Entity
@Table(name = "order_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
public class Order extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private OrderDeliveryAddress deliveryAddress;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "stage", nullable = false)
    private Stage stage = Stage.NEW;

    private static final EnumSet<Stage> ENABLE_UPDATE_IS_URGENT_STAGE_LIST = EnumSet.of(Stage.NEW, Stage.QUOTE_APPROVAL, Stage.IN_PRODUCTION, Stage.PRODUCTION_COMPLETED);
    private static final EnumSet<Stage> ENABLE_UPDATE_DELIVERY_ADDRESS_STAGE_LIST = EnumSet.of(Stage.NEW, Stage.QUOTE_APPROVAL, Stage.IN_PRODUCTION);
    private static final EnumSet<Stage> ENABLE_MANAGE_DRAWING_STAGE_LIST = EnumSet.of(Stage.NEW, Stage.QUOTE_APPROVAL, Stage.IN_PRODUCTION);
    private static final EnumSet<Stage> ENABLE_DELETE_ORDER = EnumSet.of(Stage.NEW, Stage.QUOTE_APPROVAL);

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "manufacturing_id", nullable = false)
    private OrderManufacturing manufacturing;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "post_processing_id", nullable = false)
    private OrderPostProcessing postProcessing;

    @Column(name = "request")
    private String request;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_urgent", nullable = false, length = 1)
    private Boolean isUrgent = Boolean.FALSE;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_new_issue", nullable = false, length = 1)
    private Boolean isNewIssue;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquirer_id")
    private Acquirer acquirer;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_deleted", nullable = false, length = 1)
    private Boolean isDeleted = Boolean.FALSE;

    @Builder
    public Order(Customer customer, OrderDeliveryAddress deliveryAddress, String name, String imgUrl, OrderManufacturing manufacturing, OrderPostProcessing postProcessing, String request, Boolean isNewIssue) {
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.name = name;
        this.imgUrl = imgUrl;
        this.manufacturing = manufacturing;
        this.postProcessing = postProcessing;
        this.request = request;
        this.isNewIssue = isNewIssue;
    }

    public boolean enableUpdateIsUrgent() {
        return ENABLE_UPDATE_IS_URGENT_STAGE_LIST.contains(this.stage);
    }

    public void updateIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public boolean enableUpdateDeliveryAddress() {
        return ENABLE_UPDATE_DELIVERY_ADDRESS_STAGE_LIST.contains(this.stage);
    }

    public void updateDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress.updateDeliveryAddress(deliveryAddress);
    }

    public boolean enableManageDrawing() {
        return ENABLE_MANAGE_DRAWING_STAGE_LIST.contains(this.stage);
    }

    public boolean hasQuotation() {
        return quotation != null;
    }

    public boolean enableManageQuotation() {
        return this.stage.equals(Stage.NEW);
    }

    public void createQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public boolean enableApproveQuotation() {
        return this.stage.equals(Stage.NEW);
    }

    public void approveQuotation() {
        this.stage = Stage.QUOTE_APPROVAL;
    }

    public boolean hasPurchaseOrder() {
        return purchaseOrder != null;
    }

    public boolean enableManagePurchaseOrder() {
        return this.stage.equals(Stage.QUOTE_APPROVAL);
    }

    public void createPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public boolean enableApprovePurchaseOrder() {
        return this.stage.equals(Stage.QUOTE_APPROVAL);
    }

    public void approvePurchaseOrder() {
        this.stage = Stage.IN_PRODUCTION;
    }

    public boolean enableChangeStageToProductionCompleted() {
        return this.stage.equals(Stage.IN_PRODUCTION);
    }

    public void changeStageToProductionCompleted() {
        this.stage = Stage.PRODUCTION_COMPLETED;
    }

    public boolean hasAcquirer() {
        return acquirer != null;
    }

    public void createAcquirer(Acquirer acquirer) {
        this.acquirer = acquirer;
    }

    public boolean enableChangeStageToCompleted() {
        return this.stage.equals(Stage.PRODUCTION_COMPLETED);
    }

    public void changeStageToCompleted() {
        this.stage = Stage.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public boolean enableDelete() {
        return ENABLE_DELETE_ORDER.contains(this.stage);
    }

    public void delete() {
        this.customer = null;
        this.isDeleted = Boolean.TRUE;
    }

    public boolean hasCustomer() {
        return customer != null;
    }
}
