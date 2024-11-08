package com.laser.ordermanage.ingredient.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.ingredient.dto.request.*;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientInfoResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientInfoResponseBuilder;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientStatusResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientStatusResponseBuilder;
import com.laser.ordermanage.ingredient.exception.IngredientErrorCode;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 자재 현황 데이터 조회 성공
     */
    @Test
    public void 자재_현황_데이터_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String date = LocalDate.of(2024, 4, 1).toString();
        final GetIngredientStatusResponse expectedResponse = GetIngredientStatusResponseBuilder.build();

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
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 자재_현황_데이터_조회_실패_Header_Authorization_존재() throws Exception {
        // given
        final String date = LocalDate.of(2024, 4, 1).toString();

        // when
        final ResultActions resultActions = requestGetIngredientStatusWithOutAccessToken(date);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 자재 현황 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 자재_현황_데이터_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String date = LocalDate.of(2024, 4, 1).toString();

        // when
        final ResultActions resultActions = requestGetIngredientStatus(unauthorizedAccessToken, date);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 자재 현황 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 자재_현황_데이터_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String date = LocalDate.of(2024, 4, 1).toString();

        // when
        final ResultActions resultActions = requestGetIngredientStatus(refreshToken, date);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 자재 현황 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 자재_현황_데이터_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String date = LocalDate.of(2024, 4, 1).toString();

        // when
        final ResultActions resultActions = requestGetIngredientStatus(expiredAccessToken, date);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 자재 현황 데이터 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 자재_현황_데이터_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String date = LocalDate.of(2024, 4, 1).toString();

        // when
        final ResultActions resultActions = requestGetIngredientStatus(invalidToken, date);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 자재 추가 성공
     */
    @Test
    public void 자재_추가_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredient(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 자재_추가_실패_Header_Authorization_존재() throws Exception {
        // given
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredientWithOutAccessToken(request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 자재_추가_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredient(unauthorizedAccessToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 자재_추가_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredient(refreshToken, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 자재_추가_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredient(expiredAccessToken, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 자재 추가 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 자재_추가_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final CreateIngredientRequest request = CreateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateIngredient(invalidToken, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 자재 재고 수정 성공
     */
    @Test
    public void 자재_재고_수정_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();
        final UpdateIngredientStockRequest request2 = UpdateIngredientStockRequestBuilder.build2();

        // when - 당일 자재 재고 현황 존재 X
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        resultActions.andExpect(status().isOk());

        // when - 당일 자재 재고 현황 존재 O
        final ResultActions resultActions2 = requestUpdateIngredientStock(accessToken, ingredientId, request2);

        // then
        resultActions2.andExpect(status().isOk());
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 자재_재고_수정_실패_Header_Authorization_존재() throws Exception {
        // given
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStockWithOutAccessToken(ingredientId, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 자재_재고_수정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(unauthorizedAccessToken, ingredientId, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 자재_재고_수정_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(refreshToken, ingredientId, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 자재_재고_수정_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(expiredAccessToken, ingredientId, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 자재_재고_수정_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(invalidToken, ingredientId, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 자재에 대한 접근 권한이 없음
     */
    @Test
    public void 자재_재고_수정_실패_자재_접근_권한() throws Exception {
        // given
        final String accessTokenOfAnotherFactory = jwtBuilder.accessJwtBuildOfAnotherFactory();
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessTokenOfAnotherFactory, ingredientId, request);

        // then
        assertError(IngredientErrorCode.DENIED_ACCESS_TO_INGREDIENT, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 삭제된 자재는 수정할 수 없음
     */
    @Test
    public void 자재_재고_수정_실패_삭제된_자재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String ingredientId = "6";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        assertError(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT, resultActions);
    }

    /**
     * 자재 재고 수정 실패
     * - 실패 사유 : 자재 재고에 대한 데이터가 일치하지 않음
     */
    @Test
    public void 자재_재고_수정_실패_데이터_일치() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String ingredientId = "1";
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.invalidBuild();

        // when
        final ResultActions resultActions = requestUpdateIngredientStock(accessToken, ingredientId, request);

        // then
        assertError(IngredientErrorCode.INVALID_INGREDIENT_STOCK, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 성공
     */
    @Test
    public void 자재_정보_수정_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();
        final UpdateIngredientRequest request2 = UpdateIngredientRequestBuilder.build2();

        // when - 당일 자재 정보 존재 X
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        resultActions.andExpect(status().isOk());

        // when - 당일 자재 정보 존재 O
        final ResultActions resultActions2 = requestUpdateIngredient(accessToken, ingredientId, request2);

        // then
        resultActions2.andExpect(status().isOk());
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 자재_정보_수정_실패_Header_Authorization_존재() throws Exception {
        // given
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredientWithOutAccessToken(ingredientId, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 자재_정보_수정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(unauthorizedAccessToken, ingredientId, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 자재_정보_수정_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(refreshToken, ingredientId, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 자재_정보_수정_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(expiredAccessToken, ingredientId, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 자재_정보_수정_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(invalidToken, ingredientId, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 자재 정보 (단가, 적정재고) 수정 실패
     * - 실패 사유 : 자재에 대한 접근 권한이 없음
     */
    @Test
    public void 자재_정보_수정_실패_자재_접근_권한() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfAnotherFactory();
        final String ingredientId = "1";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

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
    public void 자재_정보_수정_실패_삭제된_자재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String ingredientId = "6";
        final UpdateIngredientRequest request = UpdateIngredientRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateIngredient(accessToken, ingredientId, request);

        // then
        assertError(IngredientErrorCode.UNABLE_UPDATE_DELETED_INGREDIENT, resultActions);
    }

    /**
     * 자재 삭제 성공
     */
    @Test
    public void 자재_삭제_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, ingredientId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 자재_삭제_실패_Header_Authorization_존재() throws Exception {
        // given
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredientWithOutAccessToken(ingredientId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 자재_삭제_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredient(unauthorizedAccessToken, ingredientId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 자재_삭제_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredient(refreshToken, ingredientId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 자재_삭제_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredient(expiredAccessToken, ingredientId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 자재_삭제_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String ingredientId = "1";

        // when
        final ResultActions resultActions = requestDeleteIngredient(invalidToken, ingredientId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 자재 삭제 실패
     * - 실패 사유 : 자재에 대한 접근 권한이 없음
     */
    @Test
    public void 자재_삭제_실패_자재_접근_권한() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfAnotherFactory();
        final String ingredientId = "1";

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
    public void 자재_삭제_실패_삭제된_자재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String ingredientId = "6";

        // when
        final ResultActions resultActions = requestDeleteIngredient(accessToken, ingredientId);

        // then
        assertError(IngredientErrorCode.UNABLE_DELETE_DELETED_INGREDIENT, resultActions);
    }

    /**
     * 자재 목록 조회 성공
     */
    @Test
    public void 자재_목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final ListResponse<GetIngredientInfoResponse> expectedResponse = new ListResponse<>(GetIngredientInfoResponseBuilder.buildList());

        // when
        final ResultActions resultActions = requestGetIngredientInfo(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<GetIngredientInfoResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<ListResponse<GetIngredientInfoResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 자재 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 자재_목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetIngredientInfoWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 자재 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 자재_목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetIngredientInfo(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 자재 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 자재_목록_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();

        // when
        final ResultActions resultActions = requestGetIngredientInfo(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 자재 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 자재_목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetIngredientInfo(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 자재 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 자재_목록_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetIngredientInfo(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    private ResultActions requestGetIngredientStatus(String accessToken, String date) throws Exception {
        return mvc.perform(get("/factory/ingredient/status")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("date", date))
                .andDo(print());
    }

    private ResultActions requestGetIngredientStatusWithOutAccessToken(String date) throws Exception {
        return mvc.perform(get("/factory/ingredient/status")
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

    private ResultActions requestCreateIngredientWithOutAccessToken(CreateIngredientRequest request) throws Exception {
        return mvc.perform(post("/factory/ingredient")
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

    private ResultActions requestUpdateIngredientStockWithOutAccessToken(String ingredientId, UpdateIngredientStockRequest request) throws Exception {
        return mvc.perform(patch("/factory/ingredient/{ingredient-id}/stock", ingredientId)
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

    private ResultActions requestUpdateIngredientWithOutAccessToken(String ingredientId, UpdateIngredientRequest request) throws Exception {
        return mvc.perform(patch("/factory/ingredient/{ingredient-id}", ingredientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteIngredient(String accessToken, String ingredientId) throws Exception {
        return mvc.perform(delete("/factory/ingredient/{ingredient-id}", ingredientId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestDeleteIngredientWithOutAccessToken(String ingredientId) throws Exception {
        return mvc.perform(delete("/factory/ingredient/{ingredient-id}", ingredientId))
                .andDo(print());
    }

    private ResultActions requestGetIngredientInfo(String accessToken) throws Exception {
        return mvc.perform(get("/factory/ingredient")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetIngredientInfoWithOutAccessToken() throws Exception {
        return mvc.perform(get("/factory/ingredient"))
                .andDo(print());
    }


}
