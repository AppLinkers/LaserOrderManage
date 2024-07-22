package com.laser.ordermanage.customer.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.api.CustomerUserAccountAPI;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequestBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponseBuilder;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import com.laser.ordermanage.customer.service.CustomerUserAccountService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.service.UserAuthService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerUserAccountAPI.class)
public class CustomerUserAccountAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserAuthService userAuthService;

    @MockBean
    private CustomerOrderService customerOrderService;

    @MockBean
    private CustomerUserAccountService customerUserAccountService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 고객 정보 조회 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_정보_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerGetCustomerAccountResponse expectedResponse = CustomerGetCustomerAccountResponseBuilder.build();

        // stub
        when(customerUserAccountService.getCustomerAccount(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetCustomerAccount(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final CustomerGetCustomerAccountResponse actualResponse = objectMapper.readValue(responseString, CustomerGetCustomerAccountResponse.class);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 고객 정보 조회 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_정보_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetCustomerAccount(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 정보 조회 실패
     * - 실패 사유 : 이메일에 해당하는 고객 정보가 존재하지 않음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_정보_조회_실패_고객_정보_존재() throws Exception {
        // given
        final String accessToken = "access-token";

        // stub
        when(customerUserAccountService.getCustomerAccount(any())).thenThrow(new CustomCommonException(CustomerErrorCode.NOT_FOUND_CUSTOMER));

        // when
        final ResultActions resultActions = requestGetCustomerAccount(accessToken);

        // then
        assertError(CustomerErrorCode.NOT_FOUND_CUSTOMER, resultActions);
    }

    /**
     * 고객 정보 변경 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_정보_변경_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_정보_변경_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(accessToken, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 고객 회사 이름 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_정보_변경_실패_고객_회사_이름_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.invalidContentBuild();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "업체 이름의 최대 글자수는 20자입니다.");
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 이메일에 해당하는 고객 정보가 존재하지 않음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_정보_변경_실패_고객_정보_존재() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(CustomerErrorCode.NOT_FOUND_CUSTOMER)).when(customerUserAccountService).updateCustomerAccount(any(), any());

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(accessToken, request);

        // then
        assertError(CustomerErrorCode.NOT_FOUND_CUSTOMER, resultActions);
    }

    /**
     * 고객 회원의 회원 탈퇴 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_회원의_회원_탈퇴_성공() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestDeleteUserAccount(accessToken);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 회원의 회원 탈퇴 성공
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_회원의_회원_탈퇴_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestDeleteUserAccount(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 회원의 회원 탈퇴 성공
     * - 실패 사유 : 이메일에 해당하는 고객 정보가 존재하지 않음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_회원의_회원_탈퇴_실패_고객_정보_존재() throws Exception {
        // given
        final String accessToken = "access-token";

        // stub
        doThrow(new CustomCommonException(CustomerErrorCode.NOT_FOUND_CUSTOMER)).when(customerUserAccountService).deleteUser(any());

        // when
        final ResultActions resultActions = requestDeleteUserAccount(accessToken);

        // then
        assertError(CustomerErrorCode.NOT_FOUND_CUSTOMER, resultActions);
    }

    private ResultActions requestGetCustomerAccount(String accessToken) throws Exception {
        return mvc.perform(get("/customer/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestUpdateCustomerAccount(String accessToken, CustomerUpdateCustomerAccountRequest request) throws Exception {
        return mvc.perform(patch("/customer/user")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteUserAccount(String accessToken) throws Exception {
        return mvc.perform(delete("/customer/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
