package com.laser.ordermanage.ingredient.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.repository.FactoryRepository;
import com.laser.ordermanage.ingredient.domain.Ingredient;
import com.laser.ordermanage.ingredient.domain.IngredientPrice;
import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.laser.ordermanage.ingredient.dto.request.CreateIngredientRequest;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientRequest;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientInfoResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientStockResponse;
import com.laser.ordermanage.ingredient.exception.IngredientErrorCode;
import com.laser.ordermanage.ingredient.repository.IngredientPriceRepository;
import com.laser.ordermanage.ingredient.repository.IngredientRepository;
import com.laser.ordermanage.ingredient.repository.IngredientStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IngredientService {

    private final FactoryRepository factoryRepository;
    private final IngredientStockRepository ingredientStockRepository;
    private final IngredientPriceRepository ingredientPriceRepository;
    private final IngredientRepository ingredientRepository;

    @Transactional(readOnly = true)
    public Ingredient getIngredientById(Long ingredientId) {
        return ingredientRepository.findFirstById(ingredientId).orElseThrow(() -> new CustomCommonException(IngredientErrorCode.NOT_FOUND_INGREDIENT));
    }

    @Transactional(readOnly = true)
    public GetIngredientStockResponse getIngredientStock(String email, LocalDate date, String unit) {
        return ingredientRepository.findIngredientStockByFactoryAndDate(email, date, unit);
    }

    @Transactional
    public void createIngredient(String email, CreateIngredientRequest request) {
        Factory factory = factoryRepository.findFirstByUserEmail(email);
        Ingredient ingredient = Ingredient.builder()
                .factory(factory)
                .texture(request.texture())
                .thickness(request.thickness())
                .width(request.width())
                .height(request.height())
                .weight(request.weight())
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        IngredientStock ingredientStock = new IngredientStock(ingredient, request.optimalStock());

        ingredientStockRepository.save(ingredientStock);

        IngredientPrice ingredientPrice = IngredientPrice.builder()
                .ingredient(savedIngredient)
                .purchase(request.price().purchase())
                .sell(request.price().sell())
                .build();

        ingredientPriceRepository.save(ingredientPrice);
    }

    @Transactional(readOnly = true)
    public String getUserEmailByIngredient(Long ingredientId) {
        return ingredientRepository.findUserEmailById(ingredientId).orElseThrow(() -> new CustomCommonException(IngredientErrorCode.NOT_FOUND_INGREDIENT));
    }

    @Transactional(readOnly = true)
    public void checkAuthorityOfIngredient(User user, Long ingredientId) {
        if (!getUserEmailByIngredient(ingredientId).equals(user.getUsername())) {
            throw new CustomCommonException(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT);
        }
    }

    @Transactional
    public void updateIngredient(Long ingredientId, UpdateIngredientRequest request) {
        Ingredient ingredient = getIngredientById(ingredientId);

        if (ingredient.getDeletedAt() != null) {
            throw new CustomCommonException(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT);
        }

        // todo : 가장 최근 자재 데이터와의 계산 일치 유무 확인
        IngredientStock ingredientStock = IngredientStock.builder()
                .ingredient(ingredient)
                .incoming(request.stock().incoming())
                .production(request.stock().production())
                .stock(request.stock().currentDay())
                .optimal(request.optimalStock())
                .build();

        ingredientStockRepository.save(ingredientStock);

        IngredientPrice ingredientPrice = IngredientPrice.builder()
                .ingredient(ingredient)
                .purchase(request.price().purchase())
                .sell(request.price().sell())
                .build();

        ingredientPriceRepository.save(ingredientPrice);
    }

    @Transactional
    public void deleteIngredient(Long ingredientId) {
        Ingredient ingredient = getIngredientById(ingredientId);

        if (ingredient.getDeletedAt() != null) {
            throw new CustomCommonException(IngredientErrorCode.UNABLE_DELETE_DELETED_INGREDIENT);
        }

        ingredient.delete();
    }

    public ListResponse<GetIngredientInfoResponse> getIngredient(String email) {
        return new ListResponse<GetIngredientInfoResponse>(ingredientRepository.findIngredientByFactory(email));
    }
}
