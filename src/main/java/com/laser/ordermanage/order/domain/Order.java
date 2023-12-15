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
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private DeliveryAddress deliveryAddress;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "stage", nullable = false)
    private Stage stage = Stage.NEW;

    private static final EnumSet<Stage> ENABLE_UPDATE_IS_URGENT_STAGE_LIST = EnumSet.of(Stage.NEW, Stage.QUOTE_APPROVAL, Stage.IN_PRODUCTION, Stage.SHIPPING);
    private static final EnumSet<Stage> ENABLE_UPDATE_DELIVERY_ADDRESS_STAGE_LIST = EnumSet.of(Stage.NEW, Stage.QUOTE_APPROVAL, Stage.IN_PRODUCTION);
    private static final EnumSet<Stage> ENABLE_MANAGE_DRAWING_STAGE_LIST = EnumSet.of(Stage.NEW, Stage.QUOTE_APPROVAL, Stage.IN_PRODUCTION);

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "manufacturing_id", nullable = false)
    private OrderManufacturing manufacturing;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_processing_id", nullable = false)
    private OrderPostProcessing postProcessing;

    @Column(name = "request")
    private String request;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_urgent", nullable = false, length = 1)
    private Boolean isUrgent = Boolean.FALSE;

    @Column(name = "completed_at", updatable = false)
    private LocalDateTime completedAt;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_new_issue", nullable = false, length = 1)
    private Boolean isNewIssue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @Builder
    public Order(Customer customer, DeliveryAddress deliveryAddress, String name, String imgUrl, OrderManufacturing manufacturing, OrderPostProcessing postProcessing, String request, Boolean isNewIssue) {
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
        this.deliveryAddress = deliveryAddress;
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
}
