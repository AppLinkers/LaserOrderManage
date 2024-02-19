package com.laser.ordermanage.ingredient.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredient_stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IngredientStock extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "incoming", nullable = false)
    private Integer incoming = 0;

    @Column(name = "production", nullable = false)
    private Integer production = 0;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "optimal")
    private Integer optimal;

    @Builder
    public IngredientStock(Ingredient ingredient, Integer incoming, Integer production, Integer stock, Integer optimal) {
        this.ingredient = ingredient;
        this.incoming = incoming;
        this.production = production;
        this.stock = stock;
        this.optimal = optimal;
    }

    public IngredientStock(Ingredient ingredient, Integer optimal) {
        this.ingredient = ingredient;
        this.optimal = optimal;
    }
}
