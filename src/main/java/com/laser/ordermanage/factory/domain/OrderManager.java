package com.laser.ordermanage.factory.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderManagerRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_manager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderManager extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factory_id", nullable = false)
    private Factory factory;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Builder
    public OrderManager(Factory factory, String name, String phone) {
        this.factory = factory;
        this.name = name;
        this.phone = phone;
    }

    public void updateProperties(FactoryCreateOrUpdateOrderManagerRequest request) {
        this.name = request.name();
        this.phone = request.phone();
    }
}
