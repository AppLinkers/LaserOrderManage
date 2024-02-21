package com.laser.ordermanage.ingredient.domain;

import com.laser.ordermanage.ingredient.dto.request.IngredientPriceRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "ingredient_price")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IngredientPrice {

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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Builder
    public IngredientPrice(Ingredient ingredient, Integer purchase, Integer sell) {
        this.ingredient = ingredient;
        this.purchase = purchase;
        this.sell = sell;
    }

    public void updatePrice(IngredientPriceRequest priceRequest) {
        this.purchase = priceRequest.purchase();
        this.sell = priceRequest.sell();
    }
}
