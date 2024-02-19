package com.laser.ordermanage.ingredient.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.factory.domain.Factory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "ingredient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Ingredient extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factory_id", nullable = false)
    private Factory factory;

    @Column(name = "texture", nullable = false, length = 20)
    private String texture;

    @Column(name = "thickness", nullable = false)
    private Double thickness;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    @Builder
    public Ingredient(Factory factory, String texture, Double thickness, Integer width, Integer height, Double weight) {
        this.factory = factory;
        this.texture = texture;
        this.thickness = thickness;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }
}
