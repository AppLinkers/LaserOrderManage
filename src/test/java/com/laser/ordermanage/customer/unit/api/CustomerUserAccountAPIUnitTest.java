package com.laser.ordermanage.customer.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.api.CustomerUserAccountAPI;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponseBuilder;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @WithMockUser(roles = "CUSTOMER")
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
    @WithMockUser(roles = "FACTORY")
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
    @WithMockUser(roles = "CUSTOMER")
    public void 고객_정보_조회_실패_고객_정보_존재() throws Exception {
        // given
        final String accessToken = "access-token";

        // stub
        when(customerUserAccountService.getCustomerAccount(any())).thenThrow(new CustomCommonException(UserErrorCode.NOT_FOUND_USER));

        // when
        final ResultActions resultActions = requestGetCustomerAccount(accessToken);

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
    }

    private ResultActions requestGetCustomerAccount(String accessToken) throws Exception {
        return mvc.perform(get("/customer/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
