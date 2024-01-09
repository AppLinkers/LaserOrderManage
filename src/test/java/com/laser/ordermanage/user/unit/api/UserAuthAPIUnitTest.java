package com.laser.ordermanage.user.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.user.api.UserAuthAPI;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.request.LoginRequestBuilder;
import com.laser.ordermanage.user.dto.response.TokenInfoResponseBuilder;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.service.UserAuthService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAuthAPI.class)
public class UserAuthAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserAuthService userAuthService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 사용자 로그인 성공
     */
    @Test
    public void 로그인_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.build();

        // stub
        when(userAuthService.login(any(), any())).thenReturn(TokenInfoResponseBuilder.build());

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("role").value(Role.ROLE_CUSTOMER.name()))
                .andExpect(jsonPath("grantType").value("Bearer"))
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andExpect(jsonPath("accessTokenExpirationTime").value(ExpireTime.ACCESS_TOKEN_EXPIRE_TIME))
                .andExpect(jsonPath("refreshTokenExpirationTime").value(ExpireTime.REFRESH_TOKEN_EXPIRE_TIME));
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 이메일 필드 - null
     */
    @Test
    public void 로그인_실패_이메일_필드_null() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .password("user1-password")
                .build();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        resultActions
                .andExpect(status().is(CommonErrorCode.INVALID_FIELDS.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(CommonErrorCode.INVALID_FIELDS.getCode()))
                .andExpect(jsonPath("message").value("이메일은 필수 입력값입니다."));
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 이메일 필드 유효성
     */
    @Test
    public void 로그인_실패_이메일_필드_유효성() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("invalid-email")
                .password("user1-password")
                .build();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        resultActions
                .andExpect(status().is(CommonErrorCode.INVALID_FIELDS.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(CommonErrorCode.INVALID_FIELDS.getCode()))
                .andExpect(jsonPath("message").value("이메일 형식에 맞지 않습니다."));
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 비밀번호 필드 - null
     */
    @Test
    public void 로그인_실패_비밀번호_필드_null() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .build();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        resultActions
                .andExpect(status().is(CommonErrorCode.INVALID_FIELDS.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(CommonErrorCode.INVALID_FIELDS.getCode()))
                .andExpect(jsonPath("message").value("비밀번호는 필수 입력값입니다."));
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 비밀번호 필드 유효성
     */
    @Test
    public void 로그인_실패_비밀번호_필드_유효성() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("invalid-password")
                .build();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        resultActions
                .andExpect(status().is(CommonErrorCode.INVALID_FIELDS.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(CommonErrorCode.INVALID_FIELDS.getCode()))
                .andExpect(jsonPath("message").value("비밀번호는 8 자리 이상 영문, 숫자, 특수문자를 사용하세요."));
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 요청 데이터 인증 실패
     * - 존재하지 않는 이메일
     * - 회원 정보와 일치하지 않는 비밀번호
     */
    @Test
    public void 로그인_실패_인증정보() throws Exception {
        // given
        final LoginRequest invalidRequest = LoginRequestBuilder.invalidBuild();

        // stub
        when(userAuthService.login(any(), any())).thenThrow(new CustomCommonException(UserErrorCode.INVALID_CREDENTIALS));

        // when
        final ResultActions resultActions = requestLogin(invalidRequest);

        // then
        resultActions
                .andExpect(status().is(UserErrorCode.INVALID_CREDENTIALS.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(UserErrorCode.INVALID_CREDENTIALS.getCode()))
                .andExpect(jsonPath("message").value(UserErrorCode.INVALID_CREDENTIALS.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 성공
     */
    @Test
    public void Access_Token_재발급_성공() throws Exception {
        // given
        final String refreshToken = "refreshToken";

        // stub
        when(userAuthService.reissue(any(), any())).thenReturn(TokenInfoResponseBuilder.build());

        // when
        final ResultActions resultActions = requestReIssue(refreshToken);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("role").value(Role.ROLE_CUSTOMER.name()))
                .andExpect(jsonPath("grantType").value("Bearer"))
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andExpect(jsonPath("accessTokenExpirationTime").value(ExpireTime.ACCESS_TOKEN_EXPIRE_TIME))
                .andExpect(jsonPath("refreshTokenExpirationTime").value(ExpireTime.REFRESH_TOKEN_EXPIRE_TIME));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 JWT Token 정보를 추가하지 않음
     */
    @Test
    public void Access_Token_재발급_실패_Cookie_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestReIssueWithOutRefreshToken();

        // then
        resultActions
                .andExpect(status().is(CommonErrorCode.MISSING_COOKIE.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(CommonErrorCode.MISSING_COOKIE.getCode()))
                .andExpect(jsonPath("message").value("refreshToken" + CommonErrorCode.MISSING_COOKIE.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 유효하지 않은 JWT Token 을 사용함.
     */
    @Test
    public void Access_Token_재발급_실패_Invalid_JWT_Token() throws Exception {
        // given
        final String invalidJwtToken = "invalid-jwt-token";

        // stub
        when(userAuthService.reissue(any(), any())).thenThrow(new CustomCommonException(UserErrorCode.INVALID_JWT_TOKEN));

        // when
        final ResultActions resultActions = requestReIssue(invalidJwtToken);

        // then
        resultActions
                .andExpect(status().is(UserErrorCode.INVALID_JWT_TOKEN.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(UserErrorCode.INVALID_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(UserErrorCode.INVALID_JWT_TOKEN.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는  JWT Token 의 유효기간 만료
     */
    @Test
    public void Access_Token_재발급_실패_Expired_JWT_Token() throws Exception {
        // given
        final String expiredJwtToken = "expired-jwt-token";

        // stub
        when(userAuthService.reissue(any(), any())).thenThrow(new CustomCommonException(UserErrorCode.EXPIRED_JWT_TOKEN));

        // when
        final ResultActions resultActions = requestReIssue(expiredJwtToken);

        // then
        resultActions
                .andExpect(status().is(UserErrorCode.EXPIRED_JWT_TOKEN.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(UserErrorCode.EXPIRED_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(UserErrorCode.EXPIRED_JWT_TOKEN.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 JWT Token 에 권한 정보가 없음
     */
    @Test
    public void Access_Token_재발급_실패_Unauthorized_JWT_Token() throws Exception {
        // given
        final String unauthorizedJwtToken = "unauthorized-jwt-token";

        // stub
        when(userAuthService.reissue(any(), any())).thenThrow(new CustomCommonException(UserErrorCode.UNAUTHORIZED_JWT_TOKEN));

        // when
        final ResultActions resultActions = requestReIssue(unauthorizedJwtToken);

        // then
        resultActions
                .andExpect(status().is(UserErrorCode.UNAUTHORIZED_JWT_TOKEN.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(UserErrorCode.UNAUTHORIZED_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(UserErrorCode.UNAUTHORIZED_JWT_TOKEN.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 유효하지 않은 Refresh Token 을 사용함.
     */
    @Test
    public void Access_Token_재발급_실패_Invalid_Refresh_Token() throws Exception {
        // given
        final String invalidRefreshToken = "invalid-refreshToken";

        // stub
        when(userAuthService.reissue(any(), any())).thenThrow(new CustomCommonException(UserErrorCode.INVALID_REFRESH_JWT_TOKEN));

        // when
        final ResultActions resultActions = requestReIssue(invalidRefreshToken);

        // then
        resultActions
                .andExpect(status().is(UserErrorCode.INVALID_REFRESH_JWT_TOKEN.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(UserErrorCode.INVALID_REFRESH_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(UserErrorCode.INVALID_REFRESH_JWT_TOKEN.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 지원되지 않는 Refresh Token 을 사용함.
     */
    @Test
    public void Access_Token_재발급_실패_Unsupported_Refresh_Token() throws Exception {
        // given
        final String unsupportedRefreshToken = "unsupported-refreshToken";

        // stub
        when(userAuthService.reissue(any(), any())).thenThrow(new CustomCommonException(UserErrorCode.UNSUPPORTED_JWT_TOKEN));

        // when
        final ResultActions resultActions = requestReIssue(unsupportedRefreshToken);

        // then
        resultActions
                .andExpect(status().is(UserErrorCode.UNSUPPORTED_JWT_TOKEN.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(UserErrorCode.UNSUPPORTED_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(UserErrorCode.UNSUPPORTED_JWT_TOKEN.getMessage()));
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 성공
     */
    @Test
    @WithMockUser
    public void 로그아웃_성공() throws Exception {
        // given
        String accessToken = "access-token";

        // stub
        doNothing().when(userAuthService).logout(any());

        // when
        final ResultActions resultActions = requestLogout(accessToken);

        // then
        resultActions
                .andExpect(status().isOk());
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 실패 사유 : 인증 정보 (Authentication) 없음.
     */
    @Test
    @WithAnonymousUser
    public void 로그아웃_실패_Anonymous_User() throws Exception {
        // given
        String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestLogout(accessToken);

        // then
        resultActions
                .andExpect(status().is(UserErrorCode.MISSING_JWT_TOKEN.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(UserErrorCode.MISSING_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(UserErrorCode.MISSING_JWT_TOKEN.getMessage()));
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 실패 사유 : 유효하지 않은 Access Token 을 사용함.
     */
    @Test
    @WithMockUser
    public void 로그아웃_실패_Invalid_Access_Token() throws Exception {
        // given
        String invalidAccessToken = "invalid-accessToken";

        // stub
        doThrow(new CustomCommonException(UserErrorCode.INVALID_ACCESS_JWT_TOKEN)).when(userAuthService).logout(any());

        // when
        final ResultActions resultActions = requestLogout(invalidAccessToken);

        // then
        resultActions
                .andExpect(status().is(UserErrorCode.INVALID_ACCESS_JWT_TOKEN.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(UserErrorCode.INVALID_ACCESS_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(UserErrorCode.INVALID_ACCESS_JWT_TOKEN.getMessage()));
    }

    private ResultActions requestLogin(LoginRequest request) throws Exception {
        return mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestReIssue(String refreshToken) throws Exception {
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        return mvc.perform(post("/user/re-issue")
                        .cookie(cookie))
                .andDo(print());
    }

    private ResultActions requestReIssueWithOutRefreshToken() throws Exception {
        return mvc.perform(post("/user/re-issue"))
                .andDo(print());
    }

    private ResultActions requestLogout(String accessToken) throws Exception {
        return mvc.perform(post("/user/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

}
