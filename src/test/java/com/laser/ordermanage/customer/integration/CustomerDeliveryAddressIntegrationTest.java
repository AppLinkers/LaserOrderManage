package com.laser.ordermanage.customer.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequestBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponseBuilder;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

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
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
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
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
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
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 배송지 목록 조회 성공
     */
    @Test
    public void 고객_배송지_목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final ListResponse<CustomerGetDeliveryAddressResponse> expectedResponse = new ListResponse<>(CustomerGetDeliveryAddressResponseBuilder.buildListOfCustomer1());

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
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_배송지_목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetDeliveryAddressListWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 배송지 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_배송지_목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetDeliveryAddressList(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 배송지 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_배송지_목록_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();

        // when
        final ResultActions resultActions = requestGetDeliveryAddressList(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }


    /**
     * 고객 배송지 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_배송지_목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetDeliveryAddressList(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 배송지 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_배송지_목록_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetDeliveryAddressList(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 성공
     */
    @Test
    public void 고객_배송지_항목_수정_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_배송지_항목_수정_실패_Header_Authorization_존재() throws Exception {
        // given
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddressWithOutAccessToken(deliveryAddressId, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_배송지_항목_수정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(unauthorizedAccessToken, deliveryAddressId, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_배송지_항목_수정_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(refreshToken, deliveryAddressId, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_배송지_항목_수정_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(expiredAccessToken, deliveryAddressId, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_배송지_항목_수정_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(invalidToken, deliveryAddressId, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 배송지에 대한 접근 권한이 없음
     */
    @Test
    public void 고객_배송지_항목_수정_실패_배송지_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessTokenOfUser2, deliveryAddressId, request);

        // then
        assertError(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 기본 배송지 해제
     */
    @Test
    public void 고객_배송지_항목_수정_실패_기본_배송지_해제() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.DefaultDeliveryAddressDisableUpdateBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertError(CustomerErrorCode.UNABLE_DEFAULT_DELIVERY_ADDRESS_DISABLE, resultActions);
    }

    /**
     * 고객 배송지 삭제 성공
     */
    @Test
    public void 고객_배송지_삭제_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String deliveryAddressId = "2";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(accessToken, deliveryAddressId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_배송지_삭제_실패_Header_Authorization_존재() throws Exception {
        // given
        final String deliveryAddressId = "2";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddressWithOutAccessToken(deliveryAddressId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_배송지_삭제_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String deliveryAddressId = "2";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(unauthorizedAccessToken, deliveryAddressId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_배송지_삭제_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String deliveryAddressId = "2";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(refreshToken, deliveryAddressId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_배송지_삭제_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String deliveryAddressId = "2";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(expiredAccessToken, deliveryAddressId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_배송지_삭제_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String deliveryAddressId = "2";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(invalidToken, deliveryAddressId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 배송지에 대한 접근 권한이 없음
     */
    @Test
    public void 고객_배송지_삭제_실패_배송지_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String deliveryAddressId = "2";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(accessTokenOfUser2, deliveryAddressId);

        // then
        assertError(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS, resultActions);
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 기본 배송지 삭제
     */
    @Test
    public void 고객_배송지_삭제_실패_기본_배송지_삭제() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String defaultDeliveryAddressId = "1";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(accessToken, defaultDeliveryAddressId);

        // then
        assertError(CustomerErrorCode.DEFAULT_DELIVERY_ADDRESS_DELETE, resultActions);
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

    private ResultActions requestUpdateDeliveryAddress(String accessToken, String deliveryAddressId, CustomerCreateOrUpdateDeliveryAddressRequest request) throws Exception {
        return mvc.perform(put("/customer/delivery-address/{delivery-address-id}", deliveryAddressId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateDeliveryAddressWithOutAccessToken(String deliveryAddressId, CustomerCreateOrUpdateDeliveryAddressRequest request) throws Exception {
        return mvc.perform(put("/customer/delivery-address/{delivery-address-id}", deliveryAddressId)
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
