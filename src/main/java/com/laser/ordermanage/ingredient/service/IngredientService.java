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
import com.laser.ordermanage.ingredient.dto.response.*;
import com.laser.ordermanage.ingredient.exception.IngredientErrorCode;
import com.laser.ordermanage.ingredient.repository.IngredientPriceRepository;
import com.laser.ordermanage.ingredient.repository.IngredientRepository;
import com.laser.ordermanage.ingredient.repository.IngredientStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public String getUserEmailByIngredient(Long ingredientId) {
        return ingredientRepository.findUserEmailById(ingredientId).orElseThrow(() -> new CustomCommonException(IngredientErrorCode.NOT_FOUND_INGREDIENT));
    }

    @Transactional(readOnly = true)
    public GetIngredientStatusResponse getIngredientStatus(String email, LocalDate date) {
        List<GetIngredientResponse> getIngredientResponseList = ingredientRepository.findIngredientStatusByFactoryAndDate(email, date);

        // average price 와 total stock 구하기
        int sumPurchasePrice = 0;
        int sumSellPrice = 0;
        int totalStockCount = 0;
        double totalStockWeight = 0.0;

        int averagePurchase = 0;
        int averageSell = 0;

        if (getIngredientResponseList.size() > 0) {
            for (GetIngredientResponse ingredientResponse : getIngredientResponseList) {
                sumPurchasePrice += ingredientResponse.price().purchase();
                sumSellPrice += ingredientResponse.price().sell();
                totalStockCount += (int) ingredientResponse.stockCount().currentDay();
                totalStockWeight += (double) ingredientResponse.stockWeight().currentDay();
            }

            averagePurchase = sumPurchasePrice / getIngredientResponseList.size();
            averageSell = sumSellPrice / getIngredientResponseList.size();
        }

        return GetIngredientStatusResponse.builder()
                .averagePrice(
                        GetIngredientPriceResponse.builder()
                                .purchase(averagePurchase)
                                .sell(averageSell)
                                .build()
                )
                .totalStock(
                        GetIngredientTotalStockResponse.builder()
                                .count(totalStockCount)
                                .weight(totalStockWeight)
                                .build()
                )
                .ingredientList(getIngredientResponseList)
                .date(date)
                .build();
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
                .ingredient(savedIngredient)
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

    @Transactional
    public void updateIngredient(Long ingredientId, UpdateIngredientRequest request) {
        Ingredient ingredient = getIngredientById(ingredientId);

        if (ingredient.getDeletedAt() != null) {
            throw new CustomCommonException(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT);
        }

        LocalDate nowDate = LocalDate.now();

        // 가장 최근 자재 데이터와의 계산 일치 유무 확인
        IngredientStock previousIngredientStock = ingredientStockRepository.findPreviousByIngredientIdAndDate(ingredientId, nowDate);
        IngredientStock.validate(previousIngredientStock, request.stock());

        // 당일 자재 재고 현황 조회 및 업데이트
        Optional<IngredientStock> ingredientStockOptional = ingredientStockRepository.findByIngredientIdAndCreatedAt(ingredientId, nowDate);
        if (ingredientStockOptional.isPresent()) {
            ingredientStockOptional.get().updateStock(request.stock(), request.optimalStock());
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

        // 당일 자재 가격 현황 조회 및 업데이트
        Optional<IngredientPrice> ingredientPriceOptional = ingredientPriceRepository.findByIngredientIdAndCreatedAt(ingredientId, nowDate);
        if (ingredientPriceOptional.isPresent()) {
            ingredientPriceOptional.get().updatePrice(request.price());
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
    public GetIngredientAnalysisResponse getIngredientAnalysisByFactory(String email, String data, Long ingredientId, String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<String> itemTypeList, String stockUnit) {
        List<GetIngredientAnalysisItemResponse> ingredientAnalysisItemList;

        if (data.equals("total")) {
            ingredientAnalysisItemList = getIngredientAnalysisAsTotalByFactory(email, timeUnit, startDate, endDate, itemUnit, itemTypeList, stockUnit);
        } else if (data.equals("average")) {
            ingredientAnalysisItemList = getIngredientAnalysisAsAverageByFactory(email, timeUnit, startDate, endDate, itemUnit, itemTypeList, stockUnit);
        } else {
            checkAuthorityOfIngredient(email, ingredientId);
            ingredientAnalysisItemList = getIngredientAnalysisAsIngredient(ingredientId, timeUnit, startDate, endDate, itemUnit, itemTypeList, stockUnit);
        }

        return GetIngredientAnalysisResponse.builder()
                .timeUnit(timeUnit)
                .startDate(startDate)
                .endDate(endDate)
                .itemList(new ListResponse<>(ingredientAnalysisItemList))
                .build();
    }

    private List<GetIngredientAnalysisItemResponse> getIngredientAnalysisAsTotalByFactory(String email, String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<String> itemTypeList, String stockUnit) {
        if (timeUnit.equals("month")) {
            if (itemUnit.equals("stock")) {
                // total - month - stock
                return ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndStockByFactory(email, startDate, endDate, itemTypeList, stockUnit);
            } else {
                // total - month - price
                return ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndPriceByFactory(email, startDate, endDate, itemTypeList);
            }
        } else {
            if (itemUnit.equals("stock")) {
                // total - year - stock
                return ingredientRepository.findIngredientAnalysisAsTotalAndYearAndStockByFactory(email, startDate, endDate, itemTypeList, stockUnit);
            } else {
                // total - year - price
                return ingredientRepository.findIngredientAnalysisAsTotalAndYearAndPriceByFactory(email, startDate, endDate, itemTypeList);
            }
        }
    }

    private List<GetIngredientAnalysisItemResponse> getIngredientAnalysisAsAverageByFactory(String email, String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<String> itemTypeList, String stockUnit) {
        if (timeUnit.equals("month")) {
            if (itemUnit.equals("stock")) {
                // average - month - stock
                return ingredientRepository.findIngredientAnalysisAsAverageAndMonthAndStockByFactory(email, startDate, endDate, itemTypeList, stockUnit);
            } else {
                // average - month - price
                return ingredientRepository.findIngredientAnalysisAsAverageAndMonthAndPriceByFactory(email, startDate, endDate, itemTypeList);
            }
        } else {
            if (itemUnit.equals("stock")) {
                // average - year - stock
                return ingredientRepository.findIngredientAnalysisAsAverageAndYearAndStockByFactory(email, startDate, endDate, itemTypeList, stockUnit);
            } else {
                // average - year - price
                return ingredientRepository.findIngredientAnalysisAsAverageAndYearAndPriceByFactory(email, startDate, endDate, itemTypeList);
            }
        }
    }

    private List<GetIngredientAnalysisItemResponse> getIngredientAnalysisAsIngredient(Long ingredientId, String timeUnit, LocalDate startDate, LocalDate endDate, String itemUnit, List<String> itemTypeList, String stockUnit) {
        if (timeUnit.equals("month")) {
            if (itemUnit.equals("stock")) {
                // ingredient - month - stock
                return ingredientRepository.findIngredientAnalysisAsIngredientAndMonthAndStock(ingredientId, startDate, endDate, itemTypeList, stockUnit);
            } else {
                // ingredient - month - price
                return ingredientRepository.findIngredientAnalysisAsIngredientAndMonthAndPrice(ingredientId, startDate, endDate, itemTypeList);
            }
        } else {
            if (itemUnit.equals("stock")) {
                // ingredient - year - stock
                return ingredientRepository.findIngredientAnalysisAsIngredientAndYearAndStock(ingredientId, startDate, endDate, itemTypeList, stockUnit);
            } else {
                // ingredient - year - price
                return ingredientRepository.findIngredientAnalysisAsIngredientAndYearAndPrice(ingredientId, startDate, endDate, itemTypeList);
            }
        }
    }

    @Transactional(readOnly = true)
    public void checkAuthorityOfIngredient(String email, Long ingredientId) {
        if (!getUserEmailByIngredient(ingredientId).equals(email)) {
            throw new CustomCommonException(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT);
        }
    }

    @Transactional
    public void createIngredientStockAndPriceMonthly(YearMonth yearMonth) {
        List<IngredientStock> ingredientStockList = new ArrayList<>();
        List<IngredientPrice> ingredientPriceList = new ArrayList<>();

        // TODO: 2024/03/04 todo: N + 1 조회 -> 1 조회 변경
        List<Ingredient> ingredientList = ingredientRepository.findByDeletedAtIsNull();
        ingredientList.forEach(
                ingredient -> {
                    IngredientStock latestIngredientStock = ingredientStockRepository.findFirstByIngredientIdOrderByCreatedAtDesc(ingredient.getId());
                    if (YearMonth.from(latestIngredientStock.getCreatedAt()).isBefore(yearMonth)) {
                        IngredientStock ingredientStock = IngredientStock.builder()
                                .ingredient(ingredient)
                                .incoming(0)
                                .production(0)
                                .stock(latestIngredientStock.getStock())
                                .optimal(latestIngredientStock.getOptimal())
                                .build();

                        ingredientStockList.add(ingredientStock);
                    }

                    IngredientPrice latestIngredientPrice = ingredientPriceRepository.findFirstByIngredientIdOrderByCreatedAtDesc(ingredient.getId());
                    if (YearMonth.from(latestIngredientPrice.getCreatedAt()).isBefore(yearMonth)) {
                        IngredientPrice ingredientPrice = IngredientPrice.builder()
                                .ingredient(ingredient)
                                .purchase(latestIngredientPrice.getPurchase())
                                .sell(latestIngredientPrice.getSell())
                                .build();

                        ingredientPriceList.add(ingredientPrice);
                    }
                }
        );

        ingredientStockRepository.saveAll(ingredientStockList);
        ingredientPriceRepository.saveAll(ingredientPriceList);
    }

}
