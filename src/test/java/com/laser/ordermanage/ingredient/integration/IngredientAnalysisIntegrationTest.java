package com.laser.ordermanage.ingredient.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientAnalysisResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientAnalysisResponseBuilder;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientAnalysisIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    private final static String startYear = LocalDate.of(2023, 1, 1).toString();
    private final static String endYear = LocalDate.of(2024, 1, 1).toString();

    private final static String startYearMonth = LocalDate.of(2023, 1, 1).toString();
    private final static String endYearMonth = LocalDate.of(2023, 12, 1).toString();

    /**
     * 자재 재고 분석 데이터 조회 성공
     */
    @ParameterizedTest
    @MethodSource("provideTestData")
    public void 자재_재고_분석_데이터_조회_성공(GetIngredientAnalysisResponse expectedResponse, String data, String ingredientId, String timeUnit, String startDate, String endDate, String itemUnit, List<String> stockItem, String stockUnit, List<String> priceItem) throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();

        // when
        final ResultActions resultActions = requestGetIngredientInfo(accessToken, data, ingredientId, timeUnit, startDate, endDate, itemUnit, stockItem, stockUnit, priceItem);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final GetIngredientAnalysisResponse actualResponse = objectMapper.readValue(responseString, GetIngredientAnalysisResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build1(), "total", null, "year", startYear, endYear, "stock", List.of("all"), "count", null),

                // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build2(), "total", null, "year", startYear, endYear, "stock", List.of("all"), "weight", null),

                // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build1(), "total", null, "year", startYear, endYear, "stock", List.of("incoming", "production", "stock", "optimal"), "count", null),

                // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build2(), "total", null, "year", startYear, endYear, "stock", List.of("incoming", "production", "stock", "optimal"), "weight", null),

                // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
                Arguments.of(GetIngredientAnalysisResponseBuilder.build3(), "total", null, "year", startYear, endYear, "price", null, null, List.of("all")),

                // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
                Arguments.of(GetIngredientAnalysisResponseBuilder.build3(), "total", null, "year", startYear, endYear, "price", null, null, List.of("purchase", "sell")),

                /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

                // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build4(), "total", null, "month", startYearMonth, endYearMonth, "stock", List.of("all"), "count", null),

                // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build5(), "total", null, "month", startYearMonth, endYearMonth, "stock", List.of("all"), "weight", null),

                // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build4(), "total", null, "month", startYearMonth, endYearMonth, "stock", List.of("incoming", "production", "stock", "optimal"), "count", null),

                // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build5(), "total", null, "month", startYearMonth, endYearMonth, "stock", List.of("incoming", "production", "stock", "optimal"), "weight", null),

                // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
                Arguments.of(GetIngredientAnalysisResponseBuilder.build6(), "total", null, "month", startYearMonth, endYearMonth, "price", null, null, List.of("all")),

                // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
                Arguments.of(GetIngredientAnalysisResponseBuilder.build6(), "total", null, "month", startYearMonth, endYearMonth, "price", null, null, List.of("purchase", "sell")),

                /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

                // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build7(), "average", null, "year", startYear, endYear, "stock", List.of("all"), "count", null),

                // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build8(), "average", null, "year", startYear, endYear, "stock", List.of("all"), "weight", null),

                // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build7(), "average", null, "year", startYear, endYear, "stock", List.of("incoming", "production", "stock", "optimal"), "count", null),

                // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build8(), "average", null, "year", startYear, endYear, "stock", List.of("incoming", "production", "stock", "optimal"), "weight", null),

                // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
                Arguments.of(GetIngredientAnalysisResponseBuilder.build9(), "average", null, "year", startYear, endYear, "price", null, null, List.of("all")),

                // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
                Arguments.of(GetIngredientAnalysisResponseBuilder.build9(), "average", null, "year", startYear, endYear, "price", null, null, List.of("purchase", "sell")),

                /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

                // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build10(), "average", null, "month", startYearMonth, endYearMonth, "stock", List.of("all"), "count", null),

                // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build11(), "average", null, "month", startYearMonth, endYearMonth, "stock", List.of("all"), "weight", null),

                // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build10(), "average", null, "month", startYearMonth, endYearMonth, "stock", List.of("incoming", "production", "stock", "optimal"), "count", null),

                // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build11(), "average", null, "month", startYearMonth, endYearMonth, "stock", List.of("incoming", "production", "stock", "optimal"), "weight", null),

                // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
                Arguments.of(GetIngredientAnalysisResponseBuilder.build12(), "average", null, "month", startYearMonth, endYearMonth, "price", null, null, List.of("all")),

                // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
                Arguments.of(GetIngredientAnalysisResponseBuilder.build12(), "average", null, "month", startYearMonth, endYearMonth, "price", null, null, List.of("purchase", "sell")),

                /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

                // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build13(), "ingredient", "1", "year", startYear, endYear, "stock", List.of("all"), "count", null),

                // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build14(), "ingredient", "1", "year", startYear, endYear, "stock", List.of("all"), "weight", null),

                // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build13(), "ingredient", "1", "year", startYear, endYear, "stock", List.of("incoming", "production", "stock", "optimal"), "count", null),

                // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build14(), "ingredient", "1", "year", startYear, endYear, "stock", List.of("incoming", "production", "stock", "optimal"), "weight", null),

                // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
                Arguments.of(GetIngredientAnalysisResponseBuilder.build15(), "ingredient", "1", "year", startYear, endYear, "price", null, null, List.of("all")),

                // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
                Arguments.of(GetIngredientAnalysisResponseBuilder.build15(), "ingredient", "1", "year", startYear, endYear, "price", null, null, List.of("purchase", "sell")),

                /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

                // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build16(), "ingredient", "1", "month", startYearMonth, endYearMonth, "stock", List.of("all"), "count", null),

                // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build17(), "ingredient", "1", "month", startYearMonth, endYearMonth, "stock", List.of("all"), "weight", null),

                // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
                Arguments.of(GetIngredientAnalysisResponseBuilder.build16(), "ingredient", "1", "month", startYearMonth, endYearMonth, "stock", List.of("incoming", "production", "stock", "optimal"), "count", null),

                // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
                Arguments.of(GetIngredientAnalysisResponseBuilder.build17(), "ingredient", "1", "month", startYearMonth, endYearMonth, "stock", List.of("incoming", "production", "stock", "optimal"), "weight", null),

                // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
                Arguments.of(GetIngredientAnalysisResponseBuilder.build18(), "ingredient", "1", "month", startYearMonth, endYearMonth, "price", null, null, List.of("all")),

                // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
                Arguments.of(GetIngredientAnalysisResponseBuilder.build18(), "ingredient", "1", "month", startYearMonth, endYearMonth, "price", null, null, List.of("purchase", "sell"))
        );
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 자재_재고_분석_데이터_조회_실패_Header_Authorization_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();

        // when
        final ResultActions resultActions = requestGetIngredientInfoWithOutAccessToken("total", null, "year", startYear, endYear, "stock", List.of("all"), "count", null);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 자재_재고_분석_데이터_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetIngredientInfo(unauthorizedAccessToken, "total", null, "year", startYear, endYear, "stock", List.of("all"), "count", null);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 자재_재고_분석_데이터_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        // when
        final ResultActions resultActions = requestGetIngredientInfo(refreshToken, "total", null, "year", startYear, endYear, "stock", List.of("all"), "count", null);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 자재_재고_분석_데이터_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();

        // when
        final ResultActions resultActions = requestGetIngredientInfo(expiredAccessToken, "total", null, "year", startYear, endYear, "stock", List.of("all"), "count", null);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 자재 재고 분석 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 자재_재고_분석_데이터_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetIngredientInfo(invalidToken, "total", null, "year", startYear, endYear, "stock", List.of("all"), "count", null);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
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

    private ResultActions requestGetIngredientInfoWithOutAccessToken(String data, String ingredientId, String timeUnit, String startDate, String endDate, String itemUnit, List<String> stockItem, String stockUnit, List<String> priceItem) throws Exception {
        return mvc.perform(get("/factory/ingredient/analysis")
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
