package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.order.domain.type.Stage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Long id;

    @NotNull
    @ManyToOne
    private Customer customer;

    @NotNull
    @ManyToOne
    private DeliveryAddress deliveryAddress;

    @NotNull
    private String name;

    @NotNull
    private String imgUrl;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Stage stage = Stage.NEW;

    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST)
    private OrderManufacturing manufacturing;

    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST)
    private OrderPostProcessing postProcessing;

    private String request;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isUrgent = Boolean.FALSE;

    private LocalDateTime completedAt;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isNewIssue;

    @OneToOne
    private Quotation quotation;

    @OneToOne
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
