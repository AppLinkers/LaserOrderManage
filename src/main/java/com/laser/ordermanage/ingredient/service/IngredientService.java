package com.laser.ordermanage.ingredient.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.repository.FactoryRepository;
import com.laser.ordermanage.ingredient.domain.Ingredient;
import com.laser.ordermanage.ingredient.domain.IngredientPrice;
import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.laser.ordermanage.ingredient.domain.type.IngredientPriceType;
import com.laser.ordermanage.ingredient.domain.type.IngredientStockType;
import com.laser.ordermanage.ingredient.dto.request.CreateIngredientRequest;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientRequest;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientAnalysisResponse;
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

        IngredientStock ingredientStock = IngredientStock.builder()
                .ingredient(ingredient)
                .incoming(0)
                .production(0)
                .stock(0)
                .optimal(request.optimalStock())
                .build();

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
        LocalDate nowDate = LocalDate.now();
        Ingredient ingredient = getIngredientById(ingredientId);

        if (ingredient.getDeletedAt() != null) {
            throw new CustomCommonException(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT);
        }

        IngredientStock latestIngredientStock = ingredientStockRepository.findFirstByIngredient_idOrderByCreatedAtDesc(ingredientId);

        // 가장 최근 자재 데이터와의 계산 일치 유무 확인
        IngredientStock.validate(latestIngredientStock, request.stock());

        if (nowDate.isEqual(latestIngredientStock.getCreatedAt())) {
            latestIngredientStock.updateStock(request.stock(), request.optimalStock());
        } else {
            IngredientStock ingredientStock = IngredientStock.builder()
                    .ingredient(ingredient)
                    .incoming(request.stock().incoming())
                    .production(request.stock().production())
                    .stock(request.stock().currentDay())
                    .optimal(request.optimalStock())
                    .build();

            ingredientStockRepository.save(ingredientStock);
        }

        IngredientPrice latestIngredientPrice = ingredientPriceRepository.findFirstByIngredient_idOrderByCreatedAtDesc(ingredientId);

        if (nowDate.isEqual(latestIngredientPrice.getCreatedAt())) {
            latestIngredientPrice.updatePrice(request.price());
        } else {
            IngredientPrice ingredientPrice = IngredientPrice.builder()
                    .ingredient(ingredient)
                    .purchase(request.price().purchase())
                    .sell(request.price().sell())
                    .build();

            ingredientPriceRepository.save(ingredientPrice);
        }
    }

    @Transactional
    public void deleteIngredient(Long ingredientId) {
        Ingredient ingredient = getIngredientById(ingredientId);

        if (ingredient.getDeletedAt() != null) {
            throw new CustomCommonException(IngredientErrorCode.UNABLE_DELETE_DELETED_INGREDIENT);
        }

        ingredient.delete();
    }

    @Transactional(readOnly = true)
    public ListResponse<GetIngredientInfoResponse> getIngredientInfoByFactory(String email) {
        return new ListResponse<>(ingredientRepository.findIngredientByFactory(email));
    }

    @Transactional(readOnly = true)
    public GetIngredientAnalysisResponse getIngredientAnalysisByFactory(String data, Long ingredientId, String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<IngredientStockType> stockTypeList, List<IngredientPriceType> priceTypeList) {
        return null;
    }

//    @Transactional(readOnly = true)
//    public GetIngredientAnalysisResponse getIngredientAnalysis(String data, Long ingredientId, String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<String> stockItem, List<String> priceItem) {
//        List<GetIngredientAnalysisItemResponse> ingredientAnalysisItemList;
//        if (data.equals("total")) {
//            ingredientAnalysisItemList = getIngredientAnalysisAsTotal(timeUnit, startDate, endDate, itemUnit, stockItem, priceItem);
//        } else if (data.equals("average")) {
//            ingredientAnalysisItemList = getIngredientAnalysisAsAverage(timeUnit, startDate, endDate, itemUnit, stockItem, priceItem);
//        } else {
//            ingredientAnalysisItemList = getIngredientAnalysisAsIngredient(ingredientId, timeUnit, startDate, endDate, itemUnit, stockItem, priceItem);
//        }
//
//        return GetIngredientAnalysisResponse.builder()
//                .timeUnit(timeUnit)
//                .startDate(startDate)
//                .endDate(endDate)
//                .itemList(new ListResponse<>(ingredientAnalysisItemList))
//                .build();
//    }
//
//    private List<GetIngredientAnalysisItemResponse> getIngredientAnalysisAsTotal(String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<String> stockItem, List<String> priceItem) {
//        if (timeUnit.equals("month")) {
//            if (itemUnit.equals("stock")) {
//                // total - month - stock
//                return ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndStock(startDate, endDate, stockItem);
//            } else {
//                // total - month - price
//                return ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndPrice(startDate, endDate, priceItem);
//            }
//        } else {
//            if (itemUnit.equals("stock")) {
//                // total - year - stock
//                return ingredientRepository.findIngredientAnalysisAsTotalAndYearAndStock(startDate, endDate, stockItem);
//            } else {
//                // total - year - price
//                return ingredientRepository.findIngredientAnalysisAsTotalAndYearAndPrice(startDate, endDate, priceItem);
//            }
//        }
//    }
//
//    private List<GetIngredientAnalysisItemResponse> getIngredientAnalysisAsAverage(String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<String> stockItem, List<String> priceItem) {
//        if (timeUnit.equals("month")) {
//            if (itemUnit.equals("stock")) {
//                // average - month - stock
//            } else {
//                // average - month - price
//            }
//        } else {
//            if (itemUnit.equals("stock")) {
//                // average - year - stock
//            } else {
//                // average - year - price
//            }
//        }
//    }
//
//    private List<GetIngredientAnalysisItemResponse> getIngredientAnalysisAsIngredient(Long ingredientId, String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<String> stockItem, List<String> priceItem) {
//        if (timeUnit.equals("month")) {
//            if (itemUnit.equals("stock")) {
//                // ingredient - month - stock
//            } else {
//                // ingredient - month - price
//            }
//        } else {
//            if (itemUnit.equals("stock")) {
//                // ingredient - year - stock
//            } else {
//                // ingredient - year - price
//            }
//        }
//    }
}
