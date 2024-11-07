package com.laser.ordermanage.ingredient.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.domain.FactoryBuilder;
import com.laser.ordermanage.factory.service.FactoryUserAccountService;
import com.laser.ordermanage.ingredient.domain.*;
import com.laser.ordermanage.ingredient.dto.request.*;
import com.laser.ordermanage.ingredient.dto.response.*;
import com.laser.ordermanage.ingredient.exception.IngredientErrorCode;
import com.laser.ordermanage.ingredient.repository.IngredientPriceRepository;
import com.laser.ordermanage.ingredient.repository.IngredientRepository;
import com.laser.ordermanage.ingredient.repository.IngredientStockRepository;
import com.laser.ordermanage.ingredient.service.IngredientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class IngredientServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientStockRepository ingredientStockRepository;

    @Mock
    private IngredientPriceRepository ingredientPriceRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private FactoryUserAccountService factoryUserAccountService;

    private final static String email = "factory@gmail.com";

    private final static String totalData = "total";
    private final static String averageData = "average";
    private final static String ingredientData = "ingredient";

    private final static Long ingredientId = 1L;
    private final static Long nullIngredientId = null;

    private final static String yearTimeUnit = "year";
    private final static String monthTimeUnit = "month";

    private final static LocalDate startYear = LocalDate.of(2023, 1, 1);
    private final static LocalDate endYear = LocalDate.of(2024, 1, 1);

    private final static LocalDate startYearMonth = LocalDate.of(2023, 1, 1);
    private final static LocalDate endYearMonth = LocalDate.of(2023, 12, 1);

    private final static String stockItemUnit = "stock";
    private final static String priceItemUnit = "price";

    private final static List<String> allItemTypeList = List.of("all");

    private final static String countStockUnit = "count";
    private final static String weightStockUnit = "weight";

    /**
     * 자재 DB id 기준으로 자재 조회 성공
     */
    @Test
    public void getIngredientById_성공() {
        // given
        final Ingredient expectedIngredient = IngredientBuilder.build();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(expectedIngredient));

        // when
        final Ingredient actualIngredient = ingredientService.getIngredientById(ingredientId);

        // then
        verify(ingredientRepository, times(1)).findFirstById(ingredientId);
        Assertions.assertThat(actualIngredient).isEqualTo(expectedIngredient);
    }

    /**
     * 자재 DB id 기준으로 자재 조회 실패
     * - 실패 사유 : 존재하지 않는 자재
     */
    @Test
    public void getIngredientById_실패_NOT_FOUND_INGREDIENT() {
        // given
        final Long unknownIngredientId = 0L;

        // stub
        when(ingredientRepository.findFirstById(unknownIngredientId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.getIngredientById(unknownIngredientId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.NOT_FOUND_INGREDIENT.getMessage());
        verify(ingredientRepository, times(1)).findFirstById(unknownIngredientId);
    }

    /**
     * 자재 현황 데이터 조회 성공
     */
    @Test
    public void getIngredientStatus_성공() {
        // given
        final LocalDate date = LocalDate.of(2024, 4, 1);
        final List<GetIngredientResponse> getIngredientResponseList = GetIngredientResponseBuilder.buildList();
        final GetIngredientStatusResponse expectedResponse = GetIngredientStatusResponseBuilder.build();

        // stub
        when(ingredientRepository.findIngredientStatusByFactoryAndDate(email, date)).thenReturn(getIngredientResponseList);

        // when
        final GetIngredientStatusResponse actualResponse = ingredientService.getIngredientStatus(email, date);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientStatusByFactoryAndDate(email, date);
    }

    /**
     * 자재 추가 성공
     */
    @Test
    public void createIngredient_성공() {
        // given
        final Factory factory = FactoryBuilder.build();
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // stub
        when(factoryUserAccountService.getFactoryByFactoryManagerUserEmail(email)).thenReturn(factory);

        // when
        ingredientService.createIngredient(email, request);

        // then
        verify(factoryUserAccountService, times(1)).getFactoryByFactoryManagerUserEmail(email);
        verify(ingredientRepository, times(1)).save(any());
        verify(ingredientStockRepository, times(1)).save(any());
        verify(ingredientPriceRepository, times(1)).save(any());
    }

    /**
     * 자재 재고 수정 성공
     * - 당일 최초 등록
     */
    @Test
    public void updateIngredientStock_성공_최초등록() {
        // given
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();
        final Ingredient ingredient = IngredientBuilder.build();
        final IngredientStock previousIngredientStock = IngredientStockBuilder.build();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(ingredientStockRepository.findPreviousByIngredientIdAndDate(eq(ingredientId), any())).thenReturn(previousIngredientStock);
        when(ingredientStockRepository.findByIngredientIdAndCreatedAt(eq(ingredientId), any())).thenReturn(Optional.empty());

        // when
        ingredientService.updateIngredientStock(ingredientId, request);

        // then
        verify(ingredientRepository, times(1)).findFirstById(ingredientId);
        verify(ingredientStockRepository, times(1)).findPreviousByIngredientIdAndDate(eq(ingredientId), any());
        verify(ingredientStockRepository, times(1)).findByIngredientIdAndCreatedAt(eq(ingredientId), any());
        verify(ingredientStockRepository, times(1)).save(any());
    }

    /**
     * 자재 재고 수정 성공
     * - 당일 재 등록
     */
    @Test
    public void updateIngredientStock_성공_재등록() {
        // given
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();
        final Ingredient ingredient = IngredientBuilder.build();
        final IngredientStock previousIngredientStock = IngredientStockBuilder.build();
        final IngredientStock todayIngredientStock = IngredientStockBuilder.build();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(ingredientStockRepository.findPreviousByIngredientIdAndDate(eq(ingredientId), any())).thenReturn(previousIngredientStock);
        when(ingredientStockRepository.findByIngredientIdAndCreatedAt(eq(ingredientId), any())).thenReturn(Optional.of(todayIngredientStock));

        // when
        ingredientService.updateIngredientStock(ingredientId, request);

        // then
        Assertions.assertThat(todayIngredientStock.getIncoming()).isEqualTo(request.incoming());
        Assertions.assertThat(todayIngredientStock.getProduction()).isEqualTo(request.production());
        Assertions.assertThat(todayIngredientStock.getStock()).isEqualTo(request.currentDay());
        verify(ingredientRepository, times(1)).findFirstById(ingredientId);
        verify(ingredientStockRepository, times(1)).findPreviousByIngredientIdAndDate(eq(ingredientId), any());
        verify(ingredientStockRepository, times(1)).findByIngredientIdAndCreatedAt(eq(ingredientId), any());
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 존재하지 않는 자재
     */
    @Test
    public void updateIngredientStock_실패_NOT_FOUND_INGREDIENT() {
        // given
        final Long unknownIngredientId = 0L;
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // stub
        when(ingredientRepository.findFirstById(unknownIngredientId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.updateIngredientStock(unknownIngredientId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.NOT_FOUND_INGREDIENT.getMessage());
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 삭제된 자재는 수정할 수 없음
     */
    @Test
    public void updateIngredientStock_실패_UNABLE_UPDATE_DELETED_INGREDIENT() {
        // given
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();
        final Ingredient ingredient = IngredientBuilder.build();
        ingredient.delete();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.updateIngredientStock(ingredientId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT.getMessage());
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 성공
     * - 당일 최초 등록
     */
    @Test
    public void updateIngredient_성공_최초등록() {
        // given
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();
        final Ingredient ingredient = IngredientBuilder.build();
        final IngredientStock previousIngredientStock = IngredientStockBuilder.build();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(ingredientStockRepository.findByIngredientIdAndCreatedAt(eq(ingredientId), any())).thenReturn(Optional.empty());
        when(ingredientStockRepository.findPreviousByIngredientIdAndDate(eq(ingredientId), any())).thenReturn(previousIngredientStock);
        when(ingredientPriceRepository.findByIngredientIdAndCreatedAt(eq(ingredientId), any())).thenReturn(Optional.empty());

        // when
        ingredientService.updateIngredient(ingredientId, request);

        // then
        verify(ingredientRepository, times(1)).findFirstById(ingredientId);
        verify(ingredientStockRepository, times(1)).findByIngredientIdAndCreatedAt(eq(ingredientId), any());
        verify(ingredientStockRepository, times(1)).findPreviousByIngredientIdAndDate(eq(ingredientId), any());
        verify(ingredientStockRepository, times(1)).save(any());
        verify(ingredientPriceRepository, times(1)).findByIngredientIdAndCreatedAt(eq(ingredientId), any());
        verify(ingredientPriceRepository, times(1)).save(any());
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 성공
     * - 당일 재 등록
     */
    @Test
    public void updateIngredient_성공_재등록() {
        // given
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();
        final Ingredient ingredient = IngredientBuilder.build();
        final IngredientStock todayIngredientStock = IngredientStockBuilder.build();
        final IngredientPrice todayIngredientPrice = IngredientPriceBuilder.build();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(ingredientStockRepository.findByIngredientIdAndCreatedAt(eq(ingredientId), any())).thenReturn(Optional.of(todayIngredientStock));
        when(ingredientPriceRepository.findByIngredientIdAndCreatedAt(eq(ingredientId), any())).thenReturn(Optional.of(todayIngredientPrice));

        // when
        ingredientService.updateIngredient(ingredientId, request);

        // then
        Assertions.assertThat(todayIngredientStock.getOptimal()).isEqualTo(request.optimalStock());
        Assertions.assertThat(todayIngredientPrice.getPurchase()).isEqualTo(request.price().purchase());
        Assertions.assertThat(todayIngredientPrice.getSell()).isEqualTo(request.price().sell());
        verify(ingredientRepository, times(1)).findFirstById(ingredientId);
        verify(ingredientStockRepository, times(1)).findByIngredientIdAndCreatedAt(eq(ingredientId), any());
        verify(ingredientPriceRepository, times(1)).findByIngredientIdAndCreatedAt(eq(ingredientId), any());
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 존재하지 않는 자재
     */
    @Test
    public void updateIngredient_실패_NOT_FOUND_INGREDIENT() {
        // given
        final Long unknownIngredientId = 0L;
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // stub
        when(ingredientRepository.findFirstById(unknownIngredientId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.updateIngredient(unknownIngredientId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.NOT_FOUND_INGREDIENT.getMessage());
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 삭제된 자재는 수정할 수 없음
     */
    @Test
    public void updateIngredient_실패_UNABLE_UPDATE_DELETED_INGREDIENT() {
        // given
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();
        final Ingredient ingredient = IngredientBuilder.build();
        ingredient.delete();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.updateIngredient(ingredientId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT.getMessage());
    }

    /**
     * 자재 삭제 성공
     */
    @Test
    public void deleteIngredient_성공() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));

        // when
        ingredientService.deleteIngredient(ingredientId);

        // then
        Assertions.assertThat(ingredient.getDeletedAt()).isNotNull();
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 존재하지 않는 자재
     */
    @Test
    public void deleteIngredient_실패_NOT_FOUND_INGREDIENT() {
        // given
        final Long unknownIngredientId = 0L;

        // stub
        when(ingredientRepository.findFirstById(unknownIngredientId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.deleteIngredient(unknownIngredientId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.NOT_FOUND_INGREDIENT.getMessage());
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 삭제된 자재는 삭제할 수 없음
     */
    @Test
    public void deleteIngredient_실패_UNABLE_DELETE_DELETED_INGREDIENT() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();
        ingredient.delete();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.deleteIngredient(ingredientId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.UNABLE_DELETE_DELETED_INGREDIENT.getMessage());
    }

    /**
     * 자재 목록 조회 성공
     */
    @Test
    public void getIngredientInfoByFactoryManager_성공() {
        // given
        final List<GetIngredientInfoResponse> expectedIngredientInfoList = GetIngredientInfoResponseBuilder.buildList();
        final ListResponse<GetIngredientInfoResponse> expectedResponse = new ListResponse<>(expectedIngredientInfoList);

        // stub
        when(ingredientRepository.findIngredientByFactoryManager(email)).thenReturn(expectedIngredientInfoList);

        // when
        final ListResponse<GetIngredientInfoResponse> actualResponse = ingredientService.getIngredientInfoByFactoryManager(email);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientByFactoryManager(email);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : total, month, stock
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_total_month_stock_성공() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList4();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build4();

        // stub
        when(ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndStockByFactoryManager(email, startYearMonth, endYearMonth, allItemTypeList, countStockUnit)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, totalData, nullIngredientId, monthTimeUnit, startYearMonth, endYearMonth, stockItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsTotalAndMonthAndStockByFactoryManager(email, startYearMonth, endYearMonth, allItemTypeList, countStockUnit);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : total, month, price
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_total_month_price_성공() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList6();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build6();

        // stub
        when(ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndPriceByFactoryManager(email, startYearMonth, endYearMonth, allItemTypeList)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, totalData, nullIngredientId, monthTimeUnit, startYearMonth, endYearMonth, priceItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsTotalAndMonthAndPriceByFactoryManager(email, startYearMonth, endYearMonth, allItemTypeList);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : total, year, stock
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_total_year_stock_성공() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList1();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build1();

        // stub
        when(ingredientRepository.findIngredientAnalysisAsTotalAndYearAndStockByFactoryManager(email, startYear, endYear, allItemTypeList, countStockUnit)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, totalData, nullIngredientId, yearTimeUnit, startYear, endYear, stockItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsTotalAndYearAndStockByFactoryManager(email, startYear, endYear, allItemTypeList, countStockUnit);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : average, year, price
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_total_year_price_성공() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList3();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build3();

        // stub
        when(ingredientRepository.findIngredientAnalysisAsTotalAndYearAndPriceByFactoryManager(email, startYear, endYear, allItemTypeList)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, totalData, nullIngredientId, yearTimeUnit, startYear, endYear, priceItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsTotalAndYearAndPriceByFactoryManager(email, startYear, endYear, allItemTypeList);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : average, month, stock
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_average_month_stock_성공() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList10();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build10();

        // stub
        when(ingredientRepository.findIngredientAnalysisAsAverageAndMonthAndStockByFactoryManager(email, startYearMonth, endYearMonth, allItemTypeList, countStockUnit)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, averageData, nullIngredientId, monthTimeUnit, startYearMonth, endYearMonth, stockItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsAverageAndMonthAndStockByFactoryManager(email, startYearMonth, endYearMonth, allItemTypeList, countStockUnit);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : average, month, price
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_average_month_price_성공() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList12();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build12();

        // stub
        when(ingredientRepository.findIngredientAnalysisAsAverageAndMonthAndPriceByFactoryManager(email, startYearMonth, endYearMonth, allItemTypeList)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, averageData, nullIngredientId, monthTimeUnit, startYearMonth, endYearMonth, priceItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsAverageAndMonthAndPriceByFactoryManager(email, startYearMonth, endYearMonth, allItemTypeList);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : average, year, stock
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_average_year_stock_성공() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList7();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build7();

        // stub
        when(ingredientRepository.findIngredientAnalysisAsAverageAndYearAndStockByFactoryManager(email, startYear, endYear, allItemTypeList, countStockUnit)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, averageData, nullIngredientId, yearTimeUnit, startYear, endYear, stockItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsAverageAndYearAndStockByFactoryManager(email, startYear, endYear, allItemTypeList, countStockUnit);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : average, year, price
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_average_year_price_성공() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList9();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build9();

        // stub
        when(ingredientRepository.findIngredientAnalysisAsAverageAndYearAndPriceByFactoryManager(email, startYear, endYear, allItemTypeList)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, averageData, nullIngredientId, yearTimeUnit, startYear, endYear, priceItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsAverageAndYearAndPriceByFactoryManager(email, startYear, endYear, allItemTypeList);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : ingredient, month, stock
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_ingredient_month_stock_성공() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();
        final Factory factory = ingredient.getFactory();
        ReflectionTestUtils.setField(factory, "id", 1L);
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList16();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build16();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(factoryUserAccountService.getFactoryByFactoryManagerUserEmail(email)).thenReturn(factory);
        when(ingredientRepository.findIngredientAnalysisAsIngredientAndMonthAndStock(ingredientId, startYearMonth, endYearMonth, allItemTypeList, countStockUnit)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, ingredientData, ingredientId, monthTimeUnit, startYearMonth, endYearMonth, stockItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsIngredientAndMonthAndStock(ingredientId, startYearMonth, endYearMonth, allItemTypeList, countStockUnit);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : ingredient, month, price
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_ingredient_month_price_성공() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();
        final Factory factory = ingredient.getFactory();
        ReflectionTestUtils.setField(factory, "id", 1L);
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList18();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build18();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(factoryUserAccountService.getFactoryByFactoryManagerUserEmail(email)).thenReturn(factory);
        when(ingredientRepository.findIngredientAnalysisAsIngredientAndMonthAndPrice(ingredientId, startYearMonth, endYearMonth, allItemTypeList)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, ingredientData, ingredientId, monthTimeUnit, startYearMonth, endYearMonth, priceItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsIngredientAndMonthAndPrice(ingredientId, startYearMonth, endYearMonth, allItemTypeList);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : ingredient, year, stock
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_ingredient_year_stock_성공() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();
        final Factory factory = ingredient.getFactory();
        ReflectionTestUtils.setField(factory, "id", 1L);
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList13();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build13();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(factoryUserAccountService.getFactoryByFactoryManagerUserEmail(email)).thenReturn(factory);
        when(ingredientRepository.findIngredientAnalysisAsIngredientAndYearAndStock(ingredientId, startYear, endYear, allItemTypeList, countStockUnit)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, ingredientData, ingredientId, yearTimeUnit, startYear, endYear, stockItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsIngredientAndYearAndStock(ingredientId, startYear, endYear, allItemTypeList, countStockUnit);
    }

    /**
     * 자재 재고 분석 데이터 조회 성공
     * - 조회 필터 : ingredient, year, price
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_ingredient_year_price_성공() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();
        final Factory factory = ingredient.getFactory();
        ReflectionTestUtils.setField(factory, "id", 1L);
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList15();
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build15();

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(factoryUserAccountService.getFactoryByFactoryManagerUserEmail(email)).thenReturn(factory);
        when(ingredientRepository.findIngredientAnalysisAsIngredientAndYearAndPrice(ingredientId, startYear, endYear, allItemTypeList)).thenReturn(expectedIngredientAnalysisItemList);

        // when
        final GetIngredientAnalysisResponse actualResponse = ingredientService.getIngredientAnalysisByFactoryManager(email, ingredientData, ingredientId, yearTimeUnit, startYear, endYear, priceItemUnit, allItemTypeList, countStockUnit);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ingredientRepository, times(1)).findIngredientAnalysisAsIngredientAndYearAndPrice(ingredientId, startYear, endYear, allItemTypeList);
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 조회 필터 : ingredient
     * - 실패 사유 : 존재하지 않는 자재
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_ingredient_실패_NOT_FOUND_INGREDIENT() {
        // given

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.getIngredientAnalysisByFactoryManager(email, ingredientData, ingredientId, monthTimeUnit, startYearMonth, endYearMonth, stockItemUnit, allItemTypeList, countStockUnit))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.NOT_FOUND_INGREDIENT.getMessage());
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 조회 필터 : ingredient
     * - 실패 사유 : 자재에 대한 접근 권한이 없음
     */
    @Test
    public void getIngredientAnalysisByFactoryManager_ingredient_실패_NOT_DENIED_ACCESS_TO_INGREDIENT() {
        // given
        final String anotherFactoryManagerEmail = "another-factory@gmail.com";
        final Ingredient ingredient = IngredientBuilder.build();
        final Factory factoryOfIngredient = ingredient.getFactory();
        ReflectionTestUtils.setField(factoryOfIngredient, "id", 1L);
        final Factory anotherFactory = FactoryBuilder.build();
        ReflectionTestUtils.setField(anotherFactory, "id", 2L);

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(factoryUserAccountService.getFactoryByFactoryManagerUserEmail(anotherFactoryManagerEmail)).thenReturn(anotherFactory);

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.getIngredientAnalysisByFactoryManager(anotherFactoryManagerEmail, ingredientData, ingredientId, monthTimeUnit, startYearMonth, endYearMonth, stockItemUnit, allItemTypeList, countStockUnit))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT.getMessage());
    }

    /**
     * 자재 DB id 에 해당하는 자재의 접근 권한 확인 성공
     */
    @Test
    public void checkAuthorityOfIngredient_성공() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();
        final Factory factory = ingredient.getFactory();
        ReflectionTestUtils.setField(factory, "id", 1L);

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(factoryUserAccountService.getFactoryByFactoryManagerUserEmail(email)).thenReturn(factory);

        // when
        ingredientService.checkAuthorityOfIngredient(email, ingredientId);

        // then
        verify(ingredientRepository, times(1)).findFirstById(ingredientId);
        verify(factoryUserAccountService, times(1)).getFactoryByFactoryManagerUserEmail(email);
    }

    /**
     * 자재 DB id 에 해당하는 자재의 접근 권한 확인 실패
     * - 실패 사유 : 존재하지 않는 자재
     */
    @Test
    public void checkAuthorityOfIngredient_실패_NOT_FOUND_INGREDIENT() {
        // given
        final Long unknownIngredientId = 0L;

        // stub
        when(ingredientRepository.findFirstById(unknownIngredientId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.checkAuthorityOfIngredient(email, unknownIngredientId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.NOT_FOUND_INGREDIENT.getMessage());
    }

    /**
     * 자재 DB id 에 해당하는 자재의 접근 권한 확인 실패
     * - 자재에 대한 접근 권한이 없음
     */
    @Test
    public void checkAuthorityOfIngredient_실패_DENIED_ACCESS_TO_INGREDIENT() {
        // given
        final String anotherFactoryManagerEmail = "another-factory@gmail.com";
        final Ingredient ingredient = IngredientBuilder.build();
        final Factory factoryOfIngredient = ingredient.getFactory();
        ReflectionTestUtils.setField(factoryOfIngredient, "id", 1L);
        final Factory anotherFactory = FactoryBuilder.build();
        ReflectionTestUtils.setField(anotherFactory, "id", 2L);

        // stub
        when(ingredientRepository.findFirstById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(factoryUserAccountService.getFactoryByFactoryManagerUserEmail(anotherFactoryManagerEmail)).thenReturn(anotherFactory);

        // when & then
        Assertions.assertThatThrownBy(() -> ingredientService.checkAuthorityOfIngredient(anotherFactoryManagerEmail, ingredientId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT.getMessage());
    }

    /**
     * 월 단위 자재 재고 및 가격 데이터 생성 성공
     */
    @Test
    public void createIngredientStockAndPriceMonthly_성공() {
        // given
        final YearMonth yearMonth = YearMonth.from(LocalDate.of(2024, 4, 1));

        final Ingredient ingredient = IngredientBuilder.build();
        ReflectionTestUtils.setField(ingredient, "id", ingredientId);
        final IngredientStock ingredientStock = IngredientStockBuilder.build();
        ReflectionTestUtils.setField(ingredientStock, "createdAt", LocalDate.of(2024, 3, 31));
        final IngredientPrice ingredientPrice = IngredientPriceBuilder.build();
        ReflectionTestUtils.setField(ingredientPrice, "createdAt", LocalDate.of(2024, 3, 31));

        final Long ingredientId2 = 2L;
        final Ingredient ingredient2 = IngredientBuilder.build();
        ReflectionTestUtils.setField(ingredient2, "id", ingredientId2);
        final IngredientStock ingredientStock2 = IngredientStockBuilder.build();
        ReflectionTestUtils.setField(ingredientStock2, "createdAt", LocalDate.of(2024, 3, 31));
        final IngredientPrice ingredientPrice2 = IngredientPriceBuilder.build();
        ReflectionTestUtils.setField(ingredientPrice2, "createdAt", LocalDate.of(2024, 3, 31));

        // stub
        when(ingredientRepository.findByDeletedAtIsNull()).thenReturn(List.of(ingredient, ingredient2));

        when(ingredientStockRepository.findFirstByIngredientIdOrderByCreatedAtDesc(ingredientId)).thenReturn(ingredientStock);
        when(ingredientPriceRepository.findFirstByIngredientIdOrderByCreatedAtDesc(ingredientId)).thenReturn(ingredientPrice);

        when(ingredientStockRepository.findFirstByIngredientIdOrderByCreatedAtDesc(ingredientId2)).thenReturn(ingredientStock2);
        when(ingredientPriceRepository.findFirstByIngredientIdOrderByCreatedAtDesc(ingredientId2)).thenReturn(ingredientPrice2);


        // when
        ingredientService.createIngredientStockAndPriceMonthly(yearMonth);

        // then
        verify(ingredientRepository, times(1)).findByDeletedAtIsNull();
        verify(ingredientStockRepository, times(2)).findFirstByIngredientIdOrderByCreatedAtDesc(any());
        verify(ingredientPriceRepository, times(2)).findFirstByIngredientIdOrderByCreatedAtDesc(any());
        verify(ingredientStockRepository, times(1)).saveAll(any());
        verify(ingredientPriceRepository, times(1)).saveAll(any());
    }
}
