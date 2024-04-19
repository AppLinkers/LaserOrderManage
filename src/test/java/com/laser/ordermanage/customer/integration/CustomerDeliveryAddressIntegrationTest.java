package com.laser.ordermanage.customer.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequestBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequest;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerDeliveryAddressIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 고객 배송지 생성 성공
     */
    @Test
    public void 고객_배송지_생성_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_배송지_생성_실패_Header_Authorization_존재() throws Exception {
        // given
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddressWithOutAccessToken(request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_배송지_생성_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(unauthorizedAccessToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_배송지_생성_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(refreshToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_배송지_생성_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(expiredAccessToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_배송지_생성_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(invalidToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    private ResultActions requestCreateDeliveryAddress(String accessToken, CustomerCreateOrUpdateDeliveryAddressRequest request) throws Exception {
        return mvc.perform(post("/customer/delivery-address")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestCreateDeliveryAddressWithOutAccessToken(CustomerCreateOrUpdateDeliveryAddressRequest request) throws Exception {
        return mvc.perform(post("/customer/delivery-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestGetDeliveryAddressList(String accessToken) throws Exception {
        return mvc.perform(get("/customer/delivery-address")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetDeliveryAddressListWithOutAccessToken() throws Exception {
        return mvc.perform(get("/customer/delivery-address"))
                .andDo(print());
    }

    private ResultActions requestUpdateDeliveryAddress(String accessToken, CustomerUpdateCustomerAccountRequest request) throws Exception {
        return mvc.perform(put("/customer/delivery-address")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateDeliveryAddressWithOutAccessToken(CustomerUpdateCustomerAccountRequest request) throws Exception {
        return mvc.perform(put("/customer/delivery-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteDeliveryAddress(String accessToken, String deliveryAddressId) throws Exception {
        return mvc.perform(delete("/customer/delivery-address/{delivery-address-id}", deliveryAddressId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestDeleteDeliveryAddressWithOutAccessToken(String deliveryAddressId) throws Exception {
        return mvc.perform(delete("/customer/delivery-address/{delivery-address-id}", deliveryAddressId))
                .andDo(print());
    }
}
