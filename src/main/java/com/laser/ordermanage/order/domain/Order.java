package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    // todo: Quotation OneToOne 으로 변경
    private Long quotation_id;

    // todo: Quotation Field 로 이동
    private Long quotation_total_cost;

    // todo: Quotation Delivery Field 로 이동
    private LocalDate quotation_delivery_date;

    @NotNull
    private String name;

    @NotNull
    private String imgUrl;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Stage stage;

    @NotNull
    @OneToOne
    private OrderManufacturing manufacturing;

    @NotNull
    @OneToOne
    private OrderPostProcessing postProcessing;

    private String request;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isUrgent;

    private LocalDateTime completedAt;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isNewIssue;
}
