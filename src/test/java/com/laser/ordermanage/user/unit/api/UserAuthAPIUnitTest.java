package com.laser.ordermanage.user.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.user.api.UserAuthAPI;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.response.TokenInfoResponseBuilder;
import com.laser.ordermanage.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
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
        final LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .build();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_FIELDS.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_FIELDS.getCode()))
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_FIELDS.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_FIELDS.getCode()))
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_FIELDS.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_FIELDS.getCode()))
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("httpStatus").value(ErrorCode.INVALID_FIELDS.getHttpStatus().name()))
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_FIELDS.getCode()))
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
        final LoginRequest request = LoginRequest.builder()
                .email("invalid-user@gmail.com")
                .password("invalid-user1-password")
                .build();

        // stub
        when(userAuthService.login(any(), any())).thenThrow(new CustomCommonException(ErrorCode.INVALID_CREDENTIALS));

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
