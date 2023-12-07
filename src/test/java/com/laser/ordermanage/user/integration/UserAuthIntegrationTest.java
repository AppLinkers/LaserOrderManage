package com.laser.ordermanage.user.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserAuthIntegrationTest extends IntegrationTest {


    @Test
    public void 로그인_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("role").value(Role.ROLE_CUSTOMER.toString()))
                .andExpect(jsonPath("grantType").value("Bearer"))
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(jsonPath("accessTokenExpirationTime").value(ExpireTime.ACCESS_TOKEN_EXPIRE_TIME));

    }

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

    @Test
    public void Access_Token_재발급_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();

        String refreshToken = Objects.requireNonNull(requestLogin(request).andReturn().getResponse().getCookie("refreshToken")).getValue();

        // when
        final ResultActions resultActions = requestReIssue(refreshToken);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("role").value(Role.ROLE_CUSTOMER.toString()))
                .andExpect(jsonPath("grantType").value("Bearer"))
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(jsonPath("accessTokenExpirationTime").value(ExpireTime.ACCESS_TOKEN_EXPIRE_TIME));
    }

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

    @Test
    public void Access_Token_재발급_실패_Token_Type() throws Exception {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();

        String response = requestLogin(request).andReturn().getResponse().getContentAsString();

        String accessToken = objectMapper.readValue(response, TokenInfoResponse.class).getAccessToken();

        // when
        final ResultActions resultActions = requestReIssue(accessToken);

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_REFRESH_JWT_TOKEN.getMessage()));
    }

    @Test
    public void Access_Token_재발급_실패_Refresh_Token() throws Exception {
        // given
        String invalidRefreshToken = "invalid-refresh-token";

        // when
        final ResultActions resultActions = requestReIssue(invalidRefreshToken);

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_JWT_TOKEN.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_JWT_TOKEN.getCode()))
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_JWT_TOKEN.getMessage()));
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
}
