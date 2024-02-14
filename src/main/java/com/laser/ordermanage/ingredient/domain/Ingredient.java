package com.laser.ordermanage.ingredient.domain;

import com.laser.ordermanage.factory.domain.Factory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Ingredient {

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
}
