package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "acquirer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Acquirer extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Column(name = "signature_file_name", nullable = false)
    private String signatureFileName;

    @Column(name = "signature_file_size", nullable = false)
    private Long signatureFileSize;

    @Column(name = "signature_file_url", nullable = false)
    private String signatureFileUrl;

    @Builder
    public Acquirer(String name, String phone, String signatureFileName, Long signatureFileSize, String signatureFileUrl) {
        this.name = name;
        this.phone = phone;
        this.signatureFileName = signatureFileName;
        this.signatureFileSize = signatureFileSize;
        this.signatureFileUrl = signatureFileUrl;
    }
}
