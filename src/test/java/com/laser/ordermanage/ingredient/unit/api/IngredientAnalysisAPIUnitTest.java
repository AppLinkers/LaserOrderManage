package com.laser.ordermanage.ingredient.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.ingredient.api.IngredientAPI;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientAnalysisResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientAnalysisResponseBuilder;
import com.laser.ordermanage.ingredient.service.IngredientService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngredientAPI.class)
public class IngredientAnalysisAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private IngredientService ingredientService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    private final static String startYear = LocalDate.of(2023, 1, 1).toString();
    private final static String endYear = LocalDate.of(2024, 1, 1).toString();

    private final static String startYearMonth = LocalDate.of(2023, 1, 1).toString();
    private final static String endYearMonth = LocalDate.of(2023, 12, 1).toString();

    private final static LocalDate nowDate = LocalDate.now();

    /**
     * 자재 재고 분석 데이터 조회 성공
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 자재_재고_분석_데이터_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final GetIngredientAnalysisResponse expectedResponse = GetIngredientAnalysisResponseBuilder.build1();

        // stub
        when(ingredientService.getIngredientAnalysisByFactoryManager(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetIngredientInfo(accessToken, "total", null, "year", startYear, endYear, "stock", List.of("all"), "count", null);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final GetIngredientAnalysisResponse actualResponse = objectMapper.readValue(responseString, GetIngredientAnalysisResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 자재_재고_분석_데이터_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetIngredientInfo(accessToken, "total", null, "year", startYear, endYear, "stock", List.of("all"), "count", null);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 실패 사유 : 잘못된 요청 파라미터
     */
    @ParameterizedTest
    @MethodSource("provideTestData")
    @WithMockUser(roles = {"FACTORY"})
    public void 자재_재고_분석_데이터_조회_실패_파라미터(ErrorCode errorCode, String expectedErrorMessage, String data, String ingredientId, String timeUnit, String startDate, String endDate, String itemUnit, List<String> stockItem, String stockUnit, List<String> priceItem) throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetIngredientInfo(accessToken, data, ingredientId, timeUnit, startDate, endDate, itemUnit, stockItem, stockUnit, priceItem);

        // then
        assertErrorWithMessage(errorCode, resultActions, expectedErrorMessage);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                // 1. data null
                Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "data", null, null, "year", startYear, endYear, "stock", List.of("all"), "count", null),

                // 2. data 유효성
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "data 파라미터가 올바르지 않습니다.", "invalid-data", null, "year", startYear, endYear, "stock", List.of("all"), "count", null),

                // 3. data = ingredient, ingredient-id null
                Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "ingredient-id", "ingredient", null, "year", startYear, endYear, "stock", List.of("all"), "count", null),

                // 4. data = ingredient, ingredient-id 유효성
                Arguments.of(CommonErrorCode.MISMATCH_PARAMETER_TYPE, "ingredient-id", "ingredient", "invalid-ingredient-id", "year", startYear, endYear, "stock", List.of("all"), "count", null),

                // 5. time-unit null
                Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "time-unit", "total", null, null, startYear, endYear, "stock", List.of("all"), "count", null),

                // 6. time-unit 유효성
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "time-unit 파라미터가 올바르지 않습니다.", "total", null, "invalid-time-unit", startYear, endYear, "stock", List.of("all"), "count", null),

                // 7. start-date null
                Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "start-date", "total", null, "year", null, endYear, "stock", List.of("all"), "count", null),

                // 8. start-date 유효성
                Arguments.of(CommonErrorCode.MISMATCH_PARAMETER_TYPE, "start-date", "total", null, "year", "invalid-start-date", endYear, "stock", List.of("all"), "count", null),

                // 9. end-date null
                Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "end-date", "total", null, "year", startYear, null, "stock", List.of("all"), "count", null),

                // 10. end-date 유효성
                Arguments.of(CommonErrorCode.MISMATCH_PARAMETER_TYPE, "end-date", "total", null, "year", startYear, "invalid-end-date", "stock", List.of("all"), "count", null),

                // 11. start-date, end-date 유효성 (조회 시작 날짜는 종료 날짜 이전이어야 합니다.)
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 시작 날짜는 종료 날짜 이전이어야 합니다.", "total", null, "year", endYear, startYear, "stock", List.of("all"), "count", null),

                // 12. time-unit = year, start-date 유효성 (조회 시작 날짜는 2023년 이후이어야 합니다.)
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 시작 날짜는 2023년 이후이어야 합니다.", "total", null, "year", LocalDate.of(2022, 1, 1).toString(), endYear, "stock", List.of("all"), "count", null),

                // 13. time-unit = month, start-date 유효성 (조회 시작 날짜는 2023년 1월 이후이어야 합니다.)
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 시작 날짜는 2023년 1월 이후이어야 합니다.", "total", null, "month", LocalDate.of(2022, 12, 1).toString(), endYearMonth, "stock", List.of("all"), "count", null),

                // 14. time-unit = year, start-date, end-date 유효성 (조회 시작 및 종료 날짜는 현재 날짜 이전이어야 합니다.)
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 시작 및 종료 날짜는 현재 날짜 이전이어야 합니다.", "total", null, "year", nowDate.plusYears(1).toString(), nowDate.plusYears(2).toString(), "stock", List.of("all"), "count", null),

                // 15. time-unit = month, start-date, end-date 유효성 (조회 시작 및 종료 날짜는 현재 날짜 이전이어야 합니다.)
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 시작 및 종료 날짜는 현재 날짜 이전이어야 합니다.", "total", null, "month", nowDate.plusMonths(1).toString(), nowDate.plusMonths(13).toString(), "stock", List.of("all"), "count", null),

                // 16. item-unit null
                Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "item-unit", "total", null, "year", startYear, endYear, null, List.of("all"), "count", null),

                // 17. item-unit 유효성
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "item-unit 파라미터가 올바르지 않습니다.", "total", null, "year", startYear, endYear, "invalid-item-unit", List.of("all"), "count", null),

                // 18. item-unit = stock, stock-item 유효성
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "stock-item 파라미터가 올바르지 않습니다.", "total", null, "year", startYear, endYear, "stock", List.of("invalid-stock-item"), "count", null),

                // 19. item-unit = stock, stock-unit null
                Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "stock-unit", "total", null, "year", startYear, endYear, "stock", List.of("all"), null, null),

                // 20. item-unit = stock, stock-unit 유효성
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "stock-unit 파라미터가 올바르지 않습니다.", "total", null, "year", startYear, endYear, "stock", List.of("all"), "invalid-stock-unit", null),

                // 21. item-unit = price, stock-item 유효성
                Arguments.of(CommonErrorCode.INVALID_PARAMETER, "price-item 파라미터가 올바르지 않습니다.", "total", null, "year", startYear, endYear, "price", null, null, List.of("ingredient-price-item"))
        );
    }


    private ResultActions requestGetIngredientInfo(String accessToken, String data, String ingredientId, String timeUnit, String startDate, String endDate, String itemUnit, List<String> stockItem, String stockUnit, List<String> priceItem) throws Exception {
        return mvc.perform(get("/factory/ingredient/analysis")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("data", data)
                        .param("ingredient-id", ingredientId)
                        .param("time-unit", timeUnit)
                        .param("start-date", startDate)
                        .param("end-date", endDate)
                        .param("item-unit", itemUnit)
                        .param("stock-item", (stockItem != null) ? String.join(",", stockItem) : "")
                        .param("stock-unit", stockUnit)
                        .param("price-item", (priceItem != null) ? String.join(",", priceItem) : ""))
                .andDo(print());
    }
}
