package com.laser.ordermanage.ingredient.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientRequest;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientStockRequest;
import com.laser.ordermanage.ingredient.exception.IngredientErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "ingredient_stock")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IngredientStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "incoming", nullable = false)
    private Integer incoming;

    @Column(name = "production", nullable = false)
    private Integer production;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "optimal")
    private Integer optimal;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Builder
    public IngredientStock(Ingredient ingredient, Integer incoming, Integer production, Integer stock, Integer optimal) {
        this.ingredient = ingredient;
        this.incoming = incoming;
        this.production = production;
        this.stock = stock;
        this.optimal = optimal;
    }

    public static void validate(IngredientStock previousStock, UpdateIngredientStockRequest stockRequest) {
        if (!((previousStock.stock + stockRequest.incoming() - stockRequest.production()) == stockRequest.currentDay())) {
            throw new CustomCommonException(IngredientErrorCode.INVALID_INGREDIENT_STOCK);
        }
    }

    public void updateStock(UpdateIngredientStockRequest stockRequest, Integer optimalRequest) {
        this.incoming += stockRequest.incoming();
        this.production += stockRequest.production();
        this.stock = stockRequest.currentDay();
        this.optimal = optimalRequest;
    }
}
