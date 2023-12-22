package com.laser.ordermanage.factory.domain;

import com.laser.ordermanage.factory.dto.request.FactoryUpdateUserAccountRequest;
import com.laser.ordermanage.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "factory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Factory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity user;

    @Column(name = "company_name", nullable = false, length = 20)
    private String companyName;

    @Column(name = "representative", nullable = false, length = 10)
    private String representative;

    @Column(name = "fax", length = 11)
    private String fax;

    public void updateProperties(FactoryUpdateUserAccountRequest request) {
        this.companyName = request.getCompanyName();
        this.representative = request.getRepresentative();
        this.user.updateProperties(request.getUser());
        this.fax = request.getFax();
    }
}
