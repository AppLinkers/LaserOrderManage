package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.user.domain.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private UserEntity user;

    @NotNull
    private String name;

    private String companyName;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isNew = Boolean.TRUE;

    @Builder
    public Customer(UserEntity user, String name, String companyName) {
        this.user = user;
        this.name = name;
        this.companyName = companyName;
    }
}
