package com.laser.ordermanage.user.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.request.LoginRequestBuilder;
import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAuthIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;


    /**
     * 사용자 로그인 성공
     */
    @Test
    public void 로그인_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.build();

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
     * - 실패 사유 : 존재하지 않는 이메일
     */
    @Test
    public void 로그인_실패_이메일() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("invalid-user@gmail.com")
                .password("user1-password")
                .build();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_CREDENTIALS.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_CREDENTIALS.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_CREDENTIALS.getMessage()));
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 회원 정보와 일치하지 않는 비밀번호
     */
    @Test
    public void 로그인_실패_비밀번호() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("invalid-user1-password")
                .build();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_CREDENTIALS.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_CREDENTIALS.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_CREDENTIALS.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 성공
     */
    @Test
    public void Access_Token_재발급_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();

        final String response = requestLogin(request).andReturn().getResponse().getContentAsString();

        final String refreshToken = objectMapper.readValue(response, TokenInfoResponse.class).getRefreshToken();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.MISSING_COOKIE.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.MISSING_COOKIE.getCode()))
                .andExpect(jsonPath("message").value("refreshToken" + ErrorCode.MISSING_COOKIE.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 Access Token 정보를 추가함 (Refresh Token 정보를 추가해야 함)
     */
    @Test
    public void Access_Token_재발급_실패_Token_Type() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();

        final String response = requestLogin(request).andReturn().getResponse().getContentAsString();

        final String accessToken = objectMapper.readValue(response, TokenInfoResponse.class).getAccessToken();

        // when
        final ResultActions resultActions = requestReIssue(accessToken);

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getMessage()));
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 유효하지 않은 JWT Token 정보를 추가함
     */
    @Test
    public void Access_Token_재발급_실패_Empty_Refresh_Token() throws Exception {
        // given
        final String emptyRefreshToken = "";

        // when
        final ResultActions resultActions = requestReIssue(emptyRefreshToken);

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getMessage()));

    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 유효하지 않은 JWT Token 정보를 추가함
     */
    @Test
    public void Access_Token_재발급_실패_Invalid_Refresh_Token() throws Exception {
        // given
        final String invalidRefreshToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestReIssue(invalidRefreshToken);

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_JWT_TOKEN.getMessage()));

    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는  JWT Token 의 유효기간 만료
     */
    @Test
    public void Access_Token_재발급_실패_Expired_Refresh_Token() throws Exception {
        // given
        final String expiredRefreshToken = jwtBuilder.expiredRefreshJwtBuild();

        // when
        final ResultActions resultActions = requestReIssue(expiredRefreshToken);

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.EXPIRED_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.EXPIRED_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.EXPIRED_JWT_TOKEN.getMessage()));

    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 JWT Token 에 권한 정보가 없음
     */
    @Test
    public void Access_Token_재발급_실패_Unauthorized_Refresh_Token() throws Exception {
        // given
        final String unauthorizedRefreshToken = jwtBuilder.unauthorizedRefreshJwtBuild();

        // when
        final ResultActions resultActions = requestReIssue(unauthorizedRefreshToken);

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.UNAUTHORIZED_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.UNAUTHORIZED_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.UNAUTHORIZED_JWT_TOKEN.getMessage()));

    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 최초 로그인 IP 주소와 요청 IP 주소가 일치하지 않음
     */
    @Test
    public void Access_Token_재발급_실패_IP_Address() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();

        final String response = requestLogin(request).andReturn().getResponse().getContentAsString();

        final String refreshToken = objectMapper.readValue(response, TokenInfoResponse.class).getRefreshToken();

        // when
        final ResultActions resultActions = requestReIssueWithDifferentIpAddress(refreshToken);

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getMessage()));

    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 성공
     */
    @Test
    public void 로그아웃_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();

        final String response = requestLogin(request).andReturn().getResponse().getContentAsString();

        final String accessToken = objectMapper.readValue(response, TokenInfoResponse.class).getAccessToken();

        // when
        final ResultActions resultActions = requestLogout(accessToken);

        // then
        resultActions
                .andExpect(status().isOk());

    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 로그아웃_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestLogoutWithOutAccessToken();

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.MISSING_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.MISSING_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.MISSING_JWT_TOKEN.getMessage()));

    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 요청 시, Header 에 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 로그아웃_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();

        // when
        final ResultActions resultActions = requestLogout(refreshToken);

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_ACCESS_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_ACCESS_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_ACCESS_JWT_TOKEN.getMessage()));

    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 있는  JWT Token 의 유효기간 만료
     */
    @Test
    public void 로그아웃_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestLogout(expiredAccessToken);

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.EXPIRED_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.EXPIRED_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.EXPIRED_JWT_TOKEN.getMessage()));

    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 JWT Token 에 권한 정보가 없음
     */
    @Test
    public void 로그아웃_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestLogout(unauthorizedAccessToken);

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.UNAUTHORIZED_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.UNAUTHORIZED_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.UNAUTHORIZED_JWT_TOKEN.getMessage()));

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


    private ResultActions requestReIssueWithDifferentIpAddress(String refreshToken) throws Exception {
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        return mvc.perform(post("/user/re-issue")
                        .with(request -> {
                            request.setRemoteAddr("256.100.100.100");
                            return request;
                        })
                        .cookie(cookie))
                .andDo(print());
    }

    private ResultActions requestLogout(String accessToken) throws Exception {
        return mvc.perform(post("/user/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestLogoutWithOutAccessToken() throws Exception {
        return mvc.perform(post("/user/logout"))
                .andDo(print());
    }
}
