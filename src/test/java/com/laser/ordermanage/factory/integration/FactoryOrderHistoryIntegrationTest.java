package com.laser.ordermanage.factory.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.factory.dto.response.*;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FactoryOrderHistoryIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    private final static Pageable pageable = PageRequest.of(0, 10);

    /**
     * 견적 대기 단계 및 재 발행 거래 목록 조회 성공
     */
    @Test
    public void 견적대기단계_및_재발행_거래목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder.build();
        final PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsReIssueHistory(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 견적 대기 단계 및 재 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 견적대기단계_및_재발행_거래목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsReIssueHistoryWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 견적 대기 단계 및 재 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 견적대기단계_및_재발행_거래목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsReIssueHistory(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 견적 대기 단계 및 재 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 견적대기단계_및_재발행_거래목록_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsReIssueHistory(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 견적 대기 단계 및 재 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 견적대기단계_및_재발행_거래목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsReIssueHistory(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 견적 대기 단계 및 재 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 견적대기단계_및_재발행_거래목록_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsReIssueHistory(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 견적 대기 단계 및 신규 발행 거래 목록 조회 성공
     */
    @Test
    public void 견적대기단계_및_신규발행_거래목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.build();
        final PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsNewIssueHistory(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 견적 대기 단계 및 신규 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 견적대기단계_및_신규발행_거래목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsNewIssueHistoryWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 견적 대기 단계 및 신규 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 견적대기단계_및_신규발행_거래목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsNewIssueHistory(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 견적 대기 단계 및 신규 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 견적대기단계_및_신규발행_거래목록_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsNewIssueHistory(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 견적 대기 단계 및 신규 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 견적대기단계_및_신규발행_거래목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsNewIssueHistory(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 견적 대기 단계 및 신규 발행 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 견적대기단계_및_신규발행_거래목록_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsNewIssueHistory(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 단계가 완료인 거래 목록 조회 성공
     */
    @Test
    public void 거래_완료_목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedTrue();
        final PageResponse<FactoryGetOrderHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size() ));

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistory(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final PageResponse<FactoryGetOrderHistoryResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<PageResponse<FactoryGetOrderHistoryResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 단계가 완료 이전인 거래 목록 조회 성공
     */
    @Test
    public void 거래_완료_이전_목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedFalseAndPage1();
        final PageResponse<FactoryGetOrderHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, 12 ));

        // when
        final ResultActions resultActions = requestGetOrderIsNotCompletedHistory(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final PageResponse<FactoryGetOrderHistoryResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<PageResponse<FactoryGetOrderHistoryResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetOrderHistoryWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNotCompletedHistory(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_목록_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();

        // when
        final ResultActions resultActions = requestGetOrderIsNotCompletedHistory(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNotCompletedHistory(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_목록_조회_실패_() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsNotCompletedHistory(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    private ResultActions requestGetOrderIsNewAndIsReIssueHistory(String accessToken) throws Exception {
        return mvc.perform(get("/factory/order/new/re-issue")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsNewAndIsReIssueHistoryWithOutAccessToken() throws Exception {
        return mvc.perform(get("/factory/order/new/re-issue"))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsNewAndIsNewIssueHistory(String accessToken) throws Exception {
        return mvc.perform(get("/factory/order/new/new-issue")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsNewAndIsNewIssueHistoryWithOutAccessToken() throws Exception {
        return mvc.perform(get("/factory/order/new/new-issue"))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsCompletedHistory(String accessToken) throws Exception {
        return mvc.perform(get("/factory/order")
                        .param("is-completed", String.valueOf(Boolean.TRUE))
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsNotCompletedHistory(String accessToken) throws Exception {
        return mvc.perform(get("/factory/order")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderHistoryWithOutAccessToken() throws Exception {
        return mvc.perform(get("/factory/order"))
                .andDo(print());
    }
}
