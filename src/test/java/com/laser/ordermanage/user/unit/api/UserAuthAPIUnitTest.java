package com.laser.ordermanage.user.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.user.api.UserAuthAPI;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.request.LoginRequestBuilder;
import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        final TokenInfoResponse expectedResponse = TokenInfoResponseBuilder.build();

        // stub
        when(userAuthService.login(any(), any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final TokenInfoResponse actualResponse = objectMapper.readValue(responseString, TokenInfoResponse.class);
        TokenInfoResponseBuilder.assertTokenInfoResponse(actualResponse, expectedResponse);
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 이메일 필드 null
     */
    @Test
    public void 로그인_실패_이메일_필드_null() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.nullEmailBuild();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일은 필수 입력값입니다.");
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 이메일 필드 유효성
     */
    @Test
    public void 로그인_실패_이메일_필드_유효성() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.invalidEmailBuild();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일 형식에 맞지 않습니다.");
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 비밀번호 필드 null
     */
    @Test
    public void 로그인_실패_비밀번호_필드_null() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.nullPasswordBuild();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "비밀번호는 필수 입력값입니다.");
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 비밀번호 필드 유효성
     */
    @Test
    public void 로그인_실패_비밀번호_필드_유효성() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.invalidPasswordBuild();

        // when
        final ResultActions resultActions = requestLogin(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "비밀번호는 8 자리 이상 영문, 숫자, 특수문자를 사용하세요.");
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
        assertError(UserErrorCode.INVALID_CREDENTIALS, resultActions);
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 성공
     */
    @Test
    public void Access_Token_재발급_성공() throws Exception {
        // given
        final String refreshToken = "refreshToken";
        final TokenInfoResponse expectedResponse = TokenInfoResponseBuilder.build();

        // stub
        when(userAuthService.reissue(any(), any())).thenReturn(TokenInfoResponseBuilder.build());

        // when
        final ResultActions resultActions = requestReIssue(refreshToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final TokenInfoResponse actualResponse = objectMapper.readValue(responseString, TokenInfoResponse.class);
        TokenInfoResponseBuilder.assertTokenInfoResponse(actualResponse, expectedResponse);
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
        when(userAuthService.reissue(any(), any())).thenThrow(new CustomCommonException(UserErrorCode.INVALID_REFRESH_TOKEN));

        // when
        final ResultActions resultActions = requestReIssue(invalidRefreshToken);

        // then
        assertError(UserErrorCode.INVALID_REFRESH_TOKEN, resultActions);
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
     * - 실패 사유 : 유효하지 않은 Access Token 을 사용함.
     */
    @Test
    @WithMockUser
    public void 로그아웃_실패_Invalid_Access_Token() throws Exception {
        // given
        String invalidAccessToken = "invalid-accessToken";

        // stub
        doThrow(new CustomCommonException(UserErrorCode.INVALID_ACCESS_TOKEN)).when(userAuthService).logout(any());

        // when
        final ResultActions resultActions = requestLogout(invalidAccessToken);

        // then
        assertError(UserErrorCode.INVALID_ACCESS_TOKEN, resultActions);
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

    private ResultActions requestLogout(String accessToken) throws Exception {
        return mvc.perform(post("/user/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

}
