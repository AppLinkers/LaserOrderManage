package com.laser.ordermanage.ingredient.unit.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.ingredient.api.IngredientAPI;
import com.laser.ordermanage.ingredient.dto.request.*;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientInfoResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientInfoResponseBuilder;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientStatusResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientStatusResponseBuilder;
import com.laser.ordermanage.ingredient.exception.IngredientErrorCode;
import com.laser.ordermanage.ingredient.service.IngredientService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngredientAPI.class)
public class IngredientAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private IngredientService ingredientService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 자재 현황 데이터 조회 성공
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 자재_현황_데이터_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String date = LocalDate.of(2024, 4, 1).toString();
        final GetIngredientStatusResponse expectedResponse = GetIngredientStatusResponseBuilder.build();

        // stub
        when(ingredientService.getIngredientStatus(any(), any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetIngredientStatus(accessToken, date);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final GetIngredientStatusResponse actualResponse = objectMapper.readValue(responseString, GetIngredientStatusResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 자재 현황 데이터 조회 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 자재_현황_데이터_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String date = LocalDate.of(2024, 4, 1).toString();

        // when
        final ResultActions resultActions = requestGetIngredientStatus(accessToken, date);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 현황 데이터 조회 실패
     * - 실패 사유 : date 파라미터 null
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 자재_현황_데이터_조회_실패_date_파라미터_null() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetIngredientStatus(accessToken, null);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "date");
    }

    /**
     * 자재 현황 데이터 조회 실패
     * - 실패 사유 : date 파라미터 유효성
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 자재_현황_데이터_조회_실패_date_파라미터_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidDate = "invalid-date";

        // when
        final ResultActions resultActions = requestGetIngredientStatus(accessToken, invalidDate);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "date");
    }

    /**
     * 자재 추가 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 자재_추가_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 자재_추가_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 재질 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_재질_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.nullTextureBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재질은 필수 입력값입니다.");
    }


    /**
     * 자재 추가 실패
     * - 실패 사유 : 재질 필드 empty
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_재질_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.emptyTextureBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재질은 필수 입력값입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 재질 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_재질_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.invalidTextureBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재질의 최대 글자수는 20자입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 두께 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_두께_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.nullThicknessBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "두께는 필수 입력값입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 두께 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_두깨_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.invalidThicknessBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "두께는 0.1 이상, 100 이사의 소수 입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 너비 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_너비_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.nullWidthBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "너비는 필수 입력값입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 너비 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_너비_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.invalidWidthBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "너비는 1 이상, 100 이하의 정수 입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 높이 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_높이_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.nullHeightBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "높이는 필수 입력값입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 높이 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_높이_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.invalidHeightBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "높이는 1 이상, 100 이하의 정수 입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 무게 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_무게_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.nullWeightBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "무게는 필수 입력값입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 무게 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_무게_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.invalidWeightBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "무게는 0.1 이상, 1,000 이사의 소수 입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 구매 단가 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_구매단가_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.nullPurchasePriceBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "구매 단가는 필수 입력값입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 구매 단가 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_구매단가_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.invalidPurchasePriceBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "구매단가 1 이상, 100,000 이하의 정수 입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 판매 단가 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_판매단가_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.nullSellPriceBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "판매 단가는 필수 입력값입니다.");
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 판매 단가 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_추가_실패_판매_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.invalidSellPriceBuild();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "판매단가는 1 이상, 100,000 이하의 정수 입니다.");
    }

    /**
     * 자재 재고 수정 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_재고_수정_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 자재_재고_수정_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : ingredient-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_재고_수정_실패_ingredient_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidIngredientId = "invalid-ingredient-id";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, invalidIngredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "ingredient-id");
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 입고 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_재고_수정_실패_입고_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.nullIncomingBuild();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "입고는 필수 입력값입니다.");
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 생산 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_재고_수정_실패_생산_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.nullProductionBuild();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "생산은 필수 입력값입니다.");
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 당일 재고 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_재고_수정_실패_당일재고_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.nullCurrentDayBuild();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "당일 재고는 필수 입력값입니다.");
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : ingredientId 에 해당하는 자재가 존재하지 않음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_재고_수정_실패_존재하지_않는_자재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String unknownIngredientId = "0";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.NOT_FOUND_INGREDIENT)).when(ingredientService).checkAuthorityOfIngredient(any(), any());

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, unknownIngredientId, request);

        // then
        assertError(IngredientErrorCode.NOT_FOUND_INGREDIENT, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 자재에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_재고_수정_실패_자재접근권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT)).when(ingredientService).checkAuthorityOfIngredient(any(), any());

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        assertError(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 삭제된 자재는 수정할 수 없음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_재고_수정_실패_삭제된자재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT)).when(ingredientService).updateIngredientStock(any(), any());

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        assertError(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 자재_정보_수정_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 자재_정보_수정_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : ingredient-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_실패_ingredient_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidIngredientId = "invalid-ingredient-id";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, invalidIngredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "ingredient-id");
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 구매 단가 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_실패_구매단가_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.nullPurchasePriceBuild();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "구매 단가는 필수 입력값입니다.");
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 구매 단가 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_실패_구매단가_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.invalidPurchasePriceBuild();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "구매단가 1 이상, 100,000 이하의 정수 입니다.");
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 판매 단가 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_실패_판매단가_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.nullSellPriceBuild();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "판매 단가는 필수 입력값입니다.");
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 판매 단가 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_실패_판매단가_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.invalidSellPriceBuild();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "판매단가는 1 이상, 100,000 이하의 정수 입니다.");
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : ingredientId 에 해당하는 자재가 존재하지 않음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_실패_존재하지_않는_자재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String unknownIngredientId = "0";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.NOT_FOUND_INGREDIENT)).when(ingredientService).checkAuthorityOfIngredient(any(), any());

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, unknownIngredientId, request);

        // then
        assertError(IngredientErrorCode.NOT_FOUND_INGREDIENT, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 자재에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_실패_자재접근권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT)).when(ingredientService).checkAuthorityOfIngredient(any(), any());

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertError(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 삭제된 자재는 수정할 수 없음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_정보_수정_실패_삭제된자재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT)).when(ingredientService).updateIngredient(any(), any());

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertError(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT, resultActions);
    }

    /**
     * 자재 삭제 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_삭제_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, ingredientId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 자재_삭제_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, ingredientId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 자재_삭제_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, ingredientId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : ingredient-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_삭제_실패_ingredient_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidIngredientId = "invalid-ingredient-id";

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, invalidIngredientId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "ingredient-id");
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : ingredientId 에 해당하는 자재가 존재하지 않음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_삭제_실패_존재하지_않는_자재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String unknownIngredientId = "0";

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.NOT_FOUND_INGREDIENT)).when(ingredientService).checkAuthorityOfIngredient(any(), any());

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, unknownIngredientId);

        // then
        assertError(IngredientErrorCode.NOT_FOUND_INGREDIENT, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 자재에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_삭제_실패_자재접근권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT)).when(ingredientService).checkAuthorityOfIngredient(any(), any());

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, ingredientId);

        // then
        assertError(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 이미 삭제된 자재는 삭제할 수 없음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_삭제_실패_삭제된자재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String ingredientId = "1";

        // stub
        doThrow(new CustomCommonException(IngredientErrorCode.UNABLE_DELETE_DELETED_INGREDIENT)).when(ingredientService).deleteIngredient(any());

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, ingredientId);

        // then
        assertError(IngredientErrorCode.UNABLE_DELETE_DELETED_INGREDIENT, resultActions);
    }

    /**
     * 자재 목록 조회 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 자재_목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final ListResponse<GetIngredientInfoResponse> expectedResponse = new ListResponse<>(GetIngredientInfoResponseBuilder.buildList());

        // stub
        when(ingredientService.getIngredientInfoByFactoryManager(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetIngredientInfo(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<GetIngredientInfoResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<ListResponse<GetIngredientInfoResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    private ResultActions requestGetIngredientStatus(String accessToken, String date) throws Exception {
        return mvc.perform(get("/factory/ingredient/status")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("date", date))
                .andDo(print());
    }

    private ResultActions requestCreateIngredient(String accessToken, CreateIngredientRequest request) throws Exception {
        return mvc.perform(post("/factory/ingredient")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateIngredientStock(String accessToken, String ingredientId, UpdateIngredientStockRequest request) throws Exception {
        return mvc.perform(patch("/factory/ingredient/{ingredient-id}/stock", ingredientId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateIngredient(String accessToken, String ingredientId, UpdateIngredientRequest request) throws Exception {
        return mvc.perform(patch("/factory/ingredient/{ingredient-id}", ingredientId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteIngredient(String accessToken, String ingredientId) throws Exception {
        return mvc.perform(delete("/factory/ingredient/{ingredient-id}", ingredientId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetIngredientInfo(String accessToken) throws Exception {
        return mvc.perform(get("/factory/ingredient")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
