package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderQuotationRequest;
import com.laser.ordermanage.order.domain.type.QuotationFileType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Embedded
    private File<QuotationFileType> file;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Builder
    public Quotation(Long totalCost, File<QuotationFileType> file, LocalDate deliveryDate) {
        this.totalCost = totalCost;
        this.file = file;
        this.deliveryDate = deliveryDate;
    }

    public void updateFile(File<QuotationFileType> file) {
        this.file = file;
    }

    public void updateProperties(FactoryCreateOrUpdateOrderQuotationRequest request) {
        this.totalCost = request.totalCost();
        this.deliveryDate = request.deliveryDate();
    }
}
