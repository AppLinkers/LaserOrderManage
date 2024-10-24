package com.laser.ordermanage.factory.unit.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.factory.api.FactoryOrderHistoryAPI;
import com.laser.ordermanage.factory.dto.response.*;
import com.laser.ordermanage.factory.service.FactoryOrderHistoryService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FactoryOrderHistoryAPI.class)
public class FactoryOrderHistoryAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private FactoryOrderHistoryService factoryOrderHistoryService;

    private final static Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 견적 대기 단계 및 재 발행 거래 목록 조회 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY"})
    public void 견적대기단계_및_재발행_거래목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder.build();
        final PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // stub
        when(factoryOrderHistoryService.getOrderIsNewAndIsReIssueHistory(any(), any(), any())).thenReturn(expectedResponse);

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
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 견적대기단계_및_재발행_거래목록_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsReIssueHistory(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 견적 대기 단계 및 신규 발행 거래 목록 조회 성공
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 견적대기단계_및_신규발행_거래목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.build();
        final PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // stub
        when(factoryOrderHistoryService.getOrderIsNewAndIsNewIssueHistory(any(), any(), any(), any())).thenReturn(expectedResponse);

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
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 견적대기단계_및_신규발행_거래목록_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.build();
        final PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // stub
        when(factoryOrderHistoryService.getOrderIsNewAndIsNewIssueHistory(any(), any(), any(), any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetOrderIsNewAndIsNewIssueHistory(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 단계가 완료인 거래 목록 조회 성공
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_완료_목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedTrue();
        final PageResponse<FactoryGetOrderHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size() ));

        // stub
        when(factoryOrderHistoryService.getOrderHistory(any(), Boolean.TRUE, any(), any(), any(), any(), any())).thenReturn(expectedResponse);

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
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_완료_이전_목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
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
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_완료_이전_목록_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedFalseAndPage1();
        final PageResponse<FactoryGetOrderHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, 12 ));

        // when
        final ResultActions resultActions = requestGetOrderIsNotCompletedHistory(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    // TODO: 10/24/24 @DateTimeFormat 검증 (?)

    /**
     * 거래 목록 조회 실패
     * - 실패 사유 : query 파라미터 유효성
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_완료_이전_목록_조회_실패_query_파라미터_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidQuery = "검색 단어".repeat(5);

        // when
        final ResultActions resultActions = requestGetOrderIsNotCompletedHistory(accessToken, invalidQuery);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "검색 단어수의 최대 글자수는 20자입니다.");
    }

    private ResultActions requestGetOrderIsNewAndIsReIssueHistory(String accessToken) throws Exception {
        return mvc.perform(get("/factory/order/new/re-issue")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsNewAndIsNewIssueHistory(String accessToken) throws Exception {
        return mvc.perform(get("/factory/order/new/new-issue")
                        .header("Authorization", "Bearer " + accessToken))
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

    private ResultActions requestGetOrderIsNotCompletedHistory(String accessToken, String query) throws Exception {
        return mvc.perform(get("/factory/order")
                        .param("query", query)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
