package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "quotation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Quotation extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "total_cost", nullable = false)
    private Long totalCost;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;
}
