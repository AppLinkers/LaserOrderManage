package com.laser.ordermanage.customer.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequestBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponseBuilder;
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

public class CustomerUserAccountIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 고객 정보 조회 성공
     */
    @Test
    public void 고객_정보_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final CustomerGetCustomerAccountResponse expectedResponse = CustomerGetCustomerAccountResponseBuilder.build();

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
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_정보_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetCustomerAccountWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_정보_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetCustomerAccount(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_정보_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();

        // when
        final ResultActions resultActions = requestGetCustomerAccount(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_정보_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetCustomerAccount(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_정보_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetCustomerAccount(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 에 해당하는 고객 정보가 존재하지 않음
     */
    @Test
    public void 고객_정보_조회_실패_고객_정보_존재() throws Exception {
        // given
        final String accessTokenOfUnknownUser = jwtBuilder.accessJwtOfUnknownCustomerBuild();

        // when
        final ResultActions resultActions = requestGetCustomerAccount(accessTokenOfUnknownUser);

        // then
        assertError(CustomerErrorCode.NOT_FOUND_CUSTOMER, resultActions);
    }

    /**
     * 고객 정보 변경 성공
     */
    @Test
    public void 고객_정보_변경_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_정보_변경_실패_Header_Authorization_존재() throws Exception {
        // given
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccountWithOutAccessToken(request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_정보_변경_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(unauthorizedAccessToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_정보_변경_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(refreshToken, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_정보_변경_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(expiredAccessToken, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_정보_변경_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(invalidToken, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 에 해당하는 고객 정보가 존재하지 않음
     */
    @Test
    public void 고객_정보_변경_실패_고객_정보_존재() throws Exception {
        // given
        final String accessTokenOfUnknownUser = jwtBuilder.accessJwtOfUnknownCustomerBuild();
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateCustomerAccount(accessTokenOfUnknownUser, request);

        // then
        assertError(CustomerErrorCode.NOT_FOUND_CUSTOMER, resultActions);
    }

    /**
     * 고객 회원의 회원 탈퇴 성공
     */
    @Test
    public void 고객_회원의_회원_탈퇴_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();

        // when
        final ResultActions resultActions = requestDeleteUserAccount(accessToken);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 회원의 회원 탈퇴 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_회원의_회원_탈퇴_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestDeleteUserAccountWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 회원의 회원 탈퇴 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_회원의_회원_탈퇴_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestDeleteUserAccount(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 회원의 회원 탈퇴 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_회원의_회원_탈퇴_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();

        // when
        final ResultActions resultActions = requestDeleteUserAccount(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 회원의 회원 탈퇴 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_회원의_회원_탈퇴_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestDeleteUserAccount(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 회원의 회원 탈퇴 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_회원의_회원_탈퇴_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestDeleteUserAccount(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 회원의 회원 탈퇴 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 에 해당하는 고객 정보가 존재하지 않음
     */
    @Test
    public void 고객_회원의_회원_탈퇴_실패_고객_정보_존재() throws Exception {
        // given
        final String accessTokenOfUnknownUser = jwtBuilder.accessJwtOfUnknownCustomerBuild();

        // when
        final ResultActions resultActions = requestDeleteUserAccount(accessTokenOfUnknownUser);

        // then
        assertError(CustomerErrorCode.NOT_FOUND_CUSTOMER, resultActions);
    }

    private ResultActions requestGetCustomerAccount(String accessToken) throws Exception {
        return mvc.perform(get("/customer/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetCustomerAccountWithOutAccessToken() throws Exception {
        return mvc.perform(get("/customer/user"))
                .andDo(print());
    }

    private ResultActions requestUpdateCustomerAccount(String accessToken, CustomerUpdateCustomerAccountRequest request) throws Exception {
        return mvc.perform(patch("/customer/user")
                .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateCustomerAccountWithOutAccessToken(CustomerUpdateCustomerAccountRequest request) throws Exception {
        return mvc.perform(patch("/customer/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteUserAccount(String accessToken) throws Exception {
        return mvc.perform(delete("/customer/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestDeleteUserAccountWithOutAccessToken() throws Exception {
        return mvc.perform(delete("/customer/user"))
                .andDo(print());
    }
}
