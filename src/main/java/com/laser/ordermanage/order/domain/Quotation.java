package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
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

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private QuotationFileType fileType;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Builder
    public Quotation(Long totalCost, String fileName, Long fileSize, String fileType, String fileUrl, LocalDate deliveryDate) {
        this.totalCost = totalCost;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = QuotationFileType.ofExtension(fileType);
        this.fileUrl = fileUrl;
        this.deliveryDate = deliveryDate;
    }

    public void updateFile(String fileName, Long fileSize, String fileType, String fileUrl) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = QuotationFileType.ofExtension(fileType);
        this.fileUrl = fileUrl;
    }

    public void updateProperties(FactoryCreateOrUpdateOrderQuotationRequest request) {
        this.totalCost = request.totalCost();
        this.deliveryDate = request.deliveryDate();
    }
}
