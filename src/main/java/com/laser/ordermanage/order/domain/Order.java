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

import java.time.LocalDateTime;

@Entity
@Table(name = "order_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private DeliveryAddress deliveryAddress;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "stage", nullable = false)
    private Stage stage = Stage.NEW;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "manufacturing_id", nullable = false)
    private OrderManufacturing manufacturing;

    @OneToOne(cascade = CascadeType.PERSIST)
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

    @OneToOne
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @OneToOne
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
}
