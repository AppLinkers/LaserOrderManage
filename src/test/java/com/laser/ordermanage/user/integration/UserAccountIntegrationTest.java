package com.laser.ordermanage.user.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.user.dto.request.*;
import com.laser.ordermanage.user.dto.response.GetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.GetUserAccountResponseBuilder;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponseBuilder;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAccountIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 고객 이메일 찾기 성공
     */
    @Test
    public void 고객_이메일_찾기_성공() throws Exception {
        // given
        final String customerName = "고객 이름 1";
        final String customerPhone = "01022221111";
        final List<GetUserEmailResponse> expectedResponse = GetUserEmailResponseBuilder.buildListForCustomer();

        // when
        final ResultActions resultActions = requestGetUserEmail(customerName, customerPhone);

        // then
        final String repsonseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<GetUserEmailResponse> actualResponse = objectMapper.readValue(repsonseString, new TypeReference<ListResponse<GetUserEmailResponse>>() {});

        Assertions.assertThat(actualResponse.totalElements()).isEqualTo(2);
        Assertions.assertThat(actualResponse.contents()).hasSameElementsAs(expectedResponse);
    }

    /**
     * 공장 담당자 이메일 찾기 성공
     */
    @Test
    public void 공장담당자_이메일_찾기_성공() throws Exception {
        // given
        final String factoryManagerName = "관리자";
        final String factoryManagerPhone = "01011111111";
        final List<GetUserEmailResponse> expectedResponse = GetUserEmailResponseBuilder.buildListForFactory();

        // when
        final ResultActions resultActions = requestGetUserEmail(factoryManagerName, factoryManagerPhone);

        // then
        final String repsonseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<GetUserEmailResponse> actualResponse = objectMapper.readValue(repsonseString, new TypeReference<ListResponse<GetUserEmailResponse>>() {});

        Assertions.assertThat(actualResponse.totalElements()).isEqualTo(1);
        Assertions.assertThat(actualResponse.contents()).hasSameElementsAs(expectedResponse);
    }

    /**
     * 이메일 찾기 성공
     */
    @Test
    public void 이메일_찾기_성공() throws Exception {
        // given
        final String name = "사용자 이름";
        final String phone = "01099999999";

        // when
        final ResultActions resultActions = requestGetUserEmail(name, phone);

        // then
        final String repsonseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<GetUserEmailResponse> actualResponse = objectMapper.readValue(repsonseString, new TypeReference<ListResponse<GetUserEmailResponse>>() {});

        Assertions.assertThat(actualResponse.totalElements()).isEqualTo(0);
        Assertions.assertThat(actualResponse.contents().size()).isEqualTo(0);
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 성공
     */
    @Test
    public void 비밀번호_찾기_이메일로_비밀번호_변경_링크_전송_성공() throws Exception {
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.build();

        // when
        final ResultActions resultActions = requestForRequestChangePasswordWithOutAuthentication(request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 실패
     * 실패 사유 : 요청 시, 요청 이메일에 해당하는 사용자가 존재하지 않음
     */
    @Test
    public void 비밀번호_찾기_이메일로_비밀번호_변경_링크_전송_실패_사용자_존재() throws Exception {
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.unknownUserBuild();

        // when
        final ResultActions resultActions = requestForRequestChangePasswordWithOutAuthentication(request);

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 소셜 계정은 비밀번호 변경 불가
     */
    @Test
    public void 비밀번호_찾기_이메일로_비밀번호_변경_링크_전송_실패_소셜_계정_비밀번호_변경_불가() throws Exception {
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.socialUserBuild();

        // when
        final ResultActions resultActions = requestForRequestChangePasswordWithOutAuthentication(request);

        // then
        assertError(UserErrorCode.SOCIAL_USER_UNABLE_TO_CHANGE_PASSWORD, resultActions);
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 성공
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(accessToken, baseUrl);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 소셜 계정은 비밀번호 변경 불가
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_소셜_계정_비밀번호_변경_불가() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfSocialCustomer();
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(accessToken, baseUrl);

        // then
        assertError(UserErrorCode.SOCIAL_USER_UNABLE_TO_CHANGE_PASSWORD, resultActions);
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_Header_Authorization_존재() throws Exception {
        // given
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        ResultActions resultActions = requestForRequestChangePasswordWithOutAccessToken(baseUrl);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        ResultActions resultActions = requestForRequestChangePassword(unauthorizedAccessToken, baseUrl);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(refreshToken, baseUrl);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(expiredAccessToken, baseUrl);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(invalidToken, baseUrl);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 에 해당하는 사용자가 존재하지 않음
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_사용자_존재() throws Exception {
        // given
        final String accessTokenOfUnknownUser = jwtBuilder.accessJwtOfUnknownCustomerBuild();
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(accessTokenOfUnknownUser, baseUrl);

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
    }

    /**
     * 비밀번호 변경 성공
     */
    @Test
    public void 비밀번호_변경_성공() throws Exception {
        // given
        비밀번호_변경_이메일로_비밀번호_변경_링크_전송_성공();
        final String changePasswordToken = jwtBuilder.changePasswordJwtBuildOfCustomer();
        final LoginRequest expectedLoginRequest = LoginRequestBuilder.newPasswordBuild();
        final ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password(expectedLoginRequest.password())
                .build();

        // given
        final ResultActions resultActions = requestChangePassword(changePasswordToken, request);

        // then
        resultActions.andExpect(status().isOk());

        // then - 새로운 비밀번호로 로그인
        final ResultActions resultActionsOfLogin = requestLogin(expectedLoginRequest);
        resultActionsOfLogin.andExpect(status().isOk());
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Change Password Token) 를 추가하지 않음
     */
    @Test
    public void 비밀번호_변경_실패_Header_Authorization_존재() throws Exception {
        // given
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangePasswordWithOutChangePasswordToken(request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : Header 에 있는 Authorization 정보 (Change Password Token) 에 권한 정보가 없음
     */
    @Test
    public void 비밀번호_변경_실패_Unauthorized_Change_Password_Token() throws Exception {
        // given
        final String unauthorizedChangePasswordToken = jwtBuilder.unauthorizedChangePasswordJwtBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangePassword(unauthorizedChangePasswordToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Access Token) 를 추가함
     */
    @Test
    public void 비밀번호_변경_실패_Token_Type() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangePassword(accessToken, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Change Password Token) 의 유효기간 만료
     */
    @Test
    public void 비밀번호_변경_실패_Expired_Change_Password_Token() throws Exception {
        // given
        final String expiredChangePasswordToken = jwtBuilder.expiredChangePasswordJwtBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangePassword(expiredChangePasswordToken, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 비밀번호_변경_실패_Invalid_Token() throws Exception {
        // given
        final String invalidChangePasswordToken = jwtBuilder.invalidJwtBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangePassword(invalidChangePasswordToken, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Change Password Token) 에 해당하는 사용자가 존재하지 않음
     */
    @Test
    public void 비밀번호_변경_실패_사용자_존재() throws Exception {
        // given
        final String changePasswordTokenOfUnknownUser = jwtBuilder.changePasswordJwtOfUnknownCustomerBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangePassword(changePasswordTokenOfUnknownUser, request);

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 소셜 계정은 비밀번호 변경 불가
     */
    @Test
    public void 비밀번호_변경_실패_소셜_계정_비밀번호_변경_불가() throws Exception {
        // given
        final String changePasswordToken = jwtBuilder.changePasswordJwtBuildOfSocialCustomer();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangePassword(changePasswordToken, request);

        // then
        assertError(UserErrorCode.SOCIAL_USER_UNABLE_TO_CHANGE_PASSWORD, resultActions);
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 존재하지 않는 changePasswordToken
     */
    @Test
    public void 비밀번호_변경_실패_비밀번호_변경_임시인증토큰_존재() throws Exception {
        // given
        final String changePasswordToken = jwtBuilder.changePasswordJwtBuildOfCustomer();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // given
        final ResultActions resultActions = requestChangePassword(changePasswordToken, request);

        // then

        // then
        assertError(UserErrorCode.NOT_FOUND_CHANGE_PASSWORD_TOKEN, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 조회 성공
     */
    @Test
    public void 마이페이지_계정_기본_정보_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final GetUserAccountResponse expectedResponse = GetUserAccountResponseBuilder.build();

        // when
        final ResultActions resultActions = requestGetUserAccount(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final GetUserAccountResponse actualResponse = objectMapper.readValue(responseString, GetUserAccountResponse.class);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 마이페이지 계정 기본 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 마이페이지_계정_기본_정보_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetUserAccountWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 마이페이지_계정_기본_정보_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetUserAccount(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 마이페이지_계정_기본_정보_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();

        // when
        final ResultActions resultActions = requestGetUserAccount(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 마이페이지_계정_기본_정보_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetUserAccount(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 마이페이지_계정_기본_정보_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetUserAccount(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 변경 성공
     */
    @Test
    public void 마이페이지_계정_기본_정보_변경_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final UpdateUserAccountRequest request = UpdateUserAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateUserAccount(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 마이페이지 계정 기본 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 마이페이지_계정_기본_정보_변경_실패_Header_Authorization_존재() throws Exception {
        // given
        final UpdateUserAccountRequest request = UpdateUserAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateUserAccountWithOutAccessToken(request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 마이페이지_계정_기본_정보_변경_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final UpdateUserAccountRequest request = UpdateUserAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateUserAccount(unauthorizedAccessToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 마이페이지_계정_기본_정보_변경_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();
        final UpdateUserAccountRequest request = UpdateUserAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateUserAccount(refreshToken, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 마이페이지_계정_기본_정보_변경_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final UpdateUserAccountRequest request = UpdateUserAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateUserAccount(expiredAccessToken, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 마이페이지_계정_기본_정보_변경_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final UpdateUserAccountRequest request = UpdateUserAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateUserAccount(invalidToken, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 마이페이지 계정 기본 정보 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 에 해당하는 사용자가 존재하지 않음
     */
    @Test
    public void 마이페이지_계정_기본_정보_변경_실패_사용자_존재() throws Exception {
        // given
        final String accessTokenOfUnknownUser = jwtBuilder.accessJwtOfUnknownCustomerBuild();
        final UpdateUserAccountRequest request = UpdateUserAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateUserAccount(accessTokenOfUnknownUser, request);

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
    }

    /**
     * 사용자 이메일 알림 설정 변경 성공
     */
    @Test
    public void 사용자_이메일_알림_설정_변경_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final Boolean isActivate = Boolean.TRUE;

        // when
        final ResultActions resultActions = requestChangeEmailNotification(accessToken, String.valueOf(isActivate));

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 사용자 이메일 알림 설정 변경 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 사용자_이메일_알림_설정_변경_실패_Header_Authorization_존재() throws Exception {
        // given
        final Boolean isActivate = Boolean.TRUE;

        // when
        final ResultActions resultActions = requestChangeEmailNotificationWithOutAccessToken(String.valueOf(isActivate));

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 사용자 이메일 알림 설정 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 사용자_이메일_알림_설정_변경_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final Boolean isActivate = Boolean.TRUE;

        // when
        final ResultActions resultActions = requestChangeEmailNotification(unauthorizedAccessToken, String.valueOf(isActivate));

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 사용자 이메일 알림 설정 변경 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 사용자_이메일_알림_설정_변경_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();
        final Boolean isActivate = Boolean.TRUE;

        // when
        final ResultActions resultActions = requestChangeEmailNotification(refreshToken, String.valueOf(isActivate));

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 사용자 이메일 알림 설정 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 사용자_이메일_알림_설정_변경_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final Boolean isActivate = Boolean.TRUE;

        // when
        final ResultActions resultActions = requestChangeEmailNotification(expiredAccessToken, String.valueOf(isActivate));

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 사용자 이메일 알림 설정 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 사용자_이메일_알림_설정_변경_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final Boolean isActivate = Boolean.TRUE;

        // when
        final ResultActions resultActions = requestChangeEmailNotification(invalidToken, String.valueOf(isActivate));

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 사용자 이메일 알림 설정 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 에 해당하는 사용자가 존재하지 않음
     */
    @Test
    public void 사용자_이메일_알림_설정_변경_실패_사용자_존재() throws Exception {
        // given
        final String accessTokenOfUnknownUser = jwtBuilder.accessJwtOfUnknownCustomerBuild();
        final Boolean isActivate = Boolean.TRUE;

        // when
        final ResultActions resultActions = requestChangeEmailNotification(accessTokenOfUnknownUser, String.valueOf(isActivate));

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
    }

    private ResultActions requestGetUserEmail(String name, String phone) throws Exception {
        return mvc.perform(get("/user/email")
                        .param("name", name)
                        .param("phone", phone))
                .andDo(print());
    }

    private ResultActions requestForRequestChangePasswordWithOutAuthentication(RequestChangePasswordRequest request) throws Exception {
        return mvc.perform(post("/user/password/email-link/without-auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestForRequestChangePassword(String accessToken, String baseUrl) throws Exception {
        return mvc.perform(post("/user/password/email-link")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("base-url", baseUrl))
                .andDo(print());
    }

    private ResultActions requestForRequestChangePasswordWithOutAccessToken(String baseUrl) throws Exception {
        return mvc.perform(post("/user/password/email-link")
                        .param("base-url", baseUrl))
                .andDo(print());
    }

    private ResultActions requestChangePassword(String changePasswordToken, ChangePasswordRequest request) throws Exception {
        return mvc.perform(patch("/user/password")
                        .header("Authorization", "Bearer " + changePasswordToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestChangePasswordWithOutChangePasswordToken(ChangePasswordRequest request) throws Exception {
        return mvc.perform(patch("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestLogin(LoginRequest request) throws Exception {
        return mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestGetUserAccount(String accessToken) throws Exception {
        return mvc.perform(get("/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetUserAccountWithOutAccessToken() throws Exception {
        return mvc.perform(get("/user"))
                .andDo(print());
    }

    private ResultActions requestUpdateUserAccount(String accessToken, UpdateUserAccountRequest request) throws Exception {
        return mvc.perform(patch("/user")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateUserAccountWithOutAccessToken(UpdateUserAccountRequest request) throws Exception {
        return mvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestChangeEmailNotification(String accessToken, String isActivate) throws Exception {
        return mvc.perform(patch("/user/email-notification")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("is-activate", isActivate))
                .andDo(print());
    }

    private ResultActions requestChangeEmailNotificationWithOutAccessToken(String isActivate) throws Exception {
        return mvc.perform(patch("/user/email-notification")
                        .param("is-activate", isActivate))
                .andDo(print());
    }
}
