package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.order.domain.type.SignatureFileType;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name="signature_file_name")),
            @AttributeOverride(name = "size", column = @Column(name="signature_file_size")),
            @AttributeOverride(name = "type", column = @Column(name="signature_file_type")),
            @AttributeOverride(name = "url", column = @Column(name="signature_file_url"))
    })
    private File<SignatureFileType> signatureFile;


    @Builder
    public Acquirer(String name, String phone, File<SignatureFileType> signatureFile) {
        this.name = name;
        this.phone = phone;
        this.signatureFile = signatureFile;
    }
}
