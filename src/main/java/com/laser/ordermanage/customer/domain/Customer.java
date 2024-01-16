package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateUserAccountRequest;
import com.laser.ordermanage.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity user;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "company_name", length = 20)
    private String companyName;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_new", nullable = false, length = 1)
    private Boolean isNew = Boolean.TRUE;

    @Builder
    public Customer(UserEntity user, String name, String companyName) {
        this.user = user;
        this.name = name;
        this.companyName = companyName;
    }

    public void updateProperties(CustomerUpdateUserAccountRequest request) {
        this.name = request.name();
        this.user.updateProperties(request.user());
        this.companyName = request.companyName();
    }

    public boolean isNewCustomer() {
        return this.isNew;
    }

    public void disableNewCustomer() {
        this.isNew = Boolean.FALSE;
    }

    public boolean hasCompanyName() {
        return this.companyName != null;
    }
}
