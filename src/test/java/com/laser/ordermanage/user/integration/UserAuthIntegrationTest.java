package com.laser.ordermanage.user.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

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
                .andExpect(header().exists("Set-Cookie"))
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


    private ResultActions requestLogin(LoginRequest request) throws Exception {
        return mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

}
