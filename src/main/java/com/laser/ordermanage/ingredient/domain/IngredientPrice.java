package com.laser.ordermanage.ingredient.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredient_price")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IngredientPrice extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "purchase", nullable = false)
    private Integer purchase;

    @Column(name = "sell", nullable = false)
    private Integer sell;

    @Builder
    public IngredientPrice(Ingredient ingredient, Integer purchase, Integer sell) {
        this.ingredient = ingredient;
        this.purchase = purchase;
        this.sell = sell;
    }
}
