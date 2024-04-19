package com.laser.ordermanage.customer.unit.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.customer.api.CustomerDeliveryAddressAPI;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequestBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponseBuilder;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerDeliveryAddressAPI.class)
public class CustomerDeliveryAddressAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 고객 배송지 생성 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_배송지_생성_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 배송지 목록 조회 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final ListResponse<CustomerGetDeliveryAddressResponse> expectedResponse = new ListResponse<>(CustomerGetDeliveryAddressResponseBuilder.buildListOfCustomer1());

        // stub
        when(customerDeliveryAddressService.getDeliveryAddressList(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetDeliveryAddressList(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<CustomerGetDeliveryAddressResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<ListResponse<CustomerGetDeliveryAddressResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 고객 배송지 목록 조회 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_배송지_목록_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetDeliveryAddressList(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    private ResultActions requestCreateDeliveryAddress(String accessToken, CustomerCreateOrUpdateDeliveryAddressRequest request) throws Exception {
        return mvc.perform(post("/customer/delivery-address")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestGetDeliveryAddressList(String accessToken) throws Exception {
        return mvc.perform(get("/customer/delivery-address")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestUpdateDeliveryAddress(String accessToken, String deliveryAddressId, CustomerCreateOrUpdateDeliveryAddressRequest request) throws Exception {
        return mvc.perform(put("/customer/delivery-address/{delivery-address-id}", deliveryAddressId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteDeliveryAddress(String accessToken, String deliveryAddressId) throws Exception {
        return mvc.perform(delete("/customer/delivery-address/{delivery-address-id}", deliveryAddressId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
