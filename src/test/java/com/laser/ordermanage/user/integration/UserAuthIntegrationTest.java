package com.laser.ordermanage.user.integration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.user.dto.request.LoginKakaoRequest;
import com.laser.ordermanage.user.dto.request.LoginKakaoRequestBuilder;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.request.LoginRequestBuilder;
import com.laser.ordermanage.user.dto.response.KakaoAccountResponse;
import com.laser.ordermanage.user.dto.response.KakaoAccountResponseBuilder;
import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
import com.laser.ordermanage.user.dto.response.TokenInfoResponseBuilder;
import com.laser.ordermanage.user.exception.UserErrorCode;
import jakarta.servlet.http.Cookie;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAuthIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    public static MockWebServer mockWebServer = new MockWebServer();

    @DynamicPropertySource
    public static void addUrlProperties(DynamicPropertyRegistry registry) {
        registry.add("kakao.account-uri", () -> "http://localhost:" + mockWebServer.getPort() + "/v2/user/me");
    }

    @BeforeAll
    public static void setUp() throws IOException {
        mockWebServer.start();
    }

    @AfterAll
    public static void shutdown() throws IOException {
        mockWebServer.shutdown();
    }

    /**
     * 사용자 기본 로그인 성공
     */
    @Test
    public void 기본_로그인_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.build();
        final TokenInfoResponse expectedResponse = TokenInfoResponseBuilder.build();

        // when
        final ResultActions resultActions = requestLoginBasic(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final TokenInfoResponse actualResponse = objectMapper.readValue(responseString, TokenInfoResponse.class);
        TokenInfoResponseBuilder.assertTokenInfoResponse(actualResponse, expectedResponse);
    }

    /**
     * 사용자 기본 로그인 실패
     * - 실패 사유 : 존재하지 않는 이메일
     */
    @Test
    public void 기본_로그인_실패_이메일() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.unknownUserBuild();

        // when
        final ResultActions resultActions = requestLoginBasic(request);

        // then
        assertError(UserErrorCode.INVALID_CREDENTIALS, resultActions);
    }

    /**
     * 사용자 기본 로그인 실패
     * - 실패 사유 : 회원 정보와 일치하지 않는 비밀번호
     */
    @Test
    public void 기본_로그인_실패_비밀번호() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.invalidCredentialBuild();

        // when
        final ResultActions resultActions = requestLoginBasic(request);

        // then
        assertError(UserErrorCode.INVALID_CREDENTIALS, resultActions);
    }

    /**
     * 사용자 카카오 로그인 성공
     */
    @Test
    public void 카카오_로그인_성공() throws Exception {
        // before
        objectMapper.registerModule(new JavaTimeModule());

        // given
        final LoginKakaoRequest request = LoginKakaoRequestBuilder.build();
        final KakaoAccountResponse expectedKakaoAccountResponse = KakaoAccountResponseBuilder.existKakaoBuild();
        final String mockResponse = objectMapper.writeValueAsString(expectedKakaoAccountResponse);
        final TokenInfoResponse expectedResponse = TokenInfoResponseBuilder.build();

        // stub
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // when
        final ResultActions resultActions = requestLoginKakao(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final TokenInfoResponse actualResponse = objectMapper.readValue(responseString, TokenInfoResponse.class);
        TokenInfoResponseBuilder.assertTokenInfoResponse(actualResponse, expectedResponse);
    }

    /**
     * 사용자 카카오 로그인 실패
     * - 실패 사유 : 존재하지 않는 사용자
     */
    @Test
    public void 카카오_로그인_실패_존재하지_않는_사용자() throws Exception {
        // before
        objectMapper.registerModule(new JavaTimeModule());

        // given
        final LoginKakaoRequest request = LoginKakaoRequestBuilder.build();
        final KakaoAccountResponse expectedKakaoAccountResponse = KakaoAccountResponseBuilder.newKakaoBuild();
        final String mockResponse = objectMapper.writeValueAsString(expectedKakaoAccountResponse);

        // stub
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // when
        final ResultActions resultActions = requestLoginKakao(request);

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
    }

    /**
     * 사용자 카카오 로그인 실패
     * - 실패 사유 : 이메일 중복
     */
    @Test
    public void 카카오_로그인_실패_이메일_중복() throws Exception {
        // before
        objectMapper.registerModule(new JavaTimeModule());

        // given
        final LoginKakaoRequest request = LoginKakaoRequestBuilder.build();
        final KakaoAccountResponse expectedKakaoAccountResponse = KakaoAccountResponseBuilder.existBasicBuild();
        final String mockResponse = objectMapper.writeValueAsString(expectedKakaoAccountResponse);

        // stub
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // when
        final ResultActions resultActions = requestLoginKakao(request);

        // then
        assertError(UserErrorCode.EXIST_BASIC_DUPLICATED_EMAIL_USER, resultActions);
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 성공
     */
    @Test
    public void Access_Token_재발급_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.build();

        final String response = requestLoginBasic(request).andReturn().getResponse().getContentAsString();
        final String refreshToken = objectMapper.readValue(response, TokenInfoResponse.class).refreshToken();

        final TokenInfoResponse expectedResponse = TokenInfoResponseBuilder.build();

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
     * - 실패 사유 : 요청 시, Cookie 에 Authorization 정보 (Refresh Token) 를 추가하지 않음
     */
    @Test
    public void Access_Token_재발급_실패_Cookie_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestReIssueWithOutRefreshToken();

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_COOKIE, resultActions, "refreshToken");
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 Authorization 정보 (Refresh Token) 에 권한 정보가 없음
     */
    @Test
    public void Access_Token_재발급_실패_Unauthorized_Refresh_Token() throws Exception {
        // given
        final String unauthorizedRefreshToken = jwtBuilder.unauthorizedRefreshJwtBuild();

        // when
        final ResultActions resultActions = requestReIssue(unauthorizedRefreshToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 다른 타입의 Authorization 정보 (Access Token) 를 추가함
     */
    @Test
    public void Access_Token_재발급_실패_Token_Type() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();

        // when
        final ResultActions resultActions = requestReIssue(accessToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 Authorization(Refresh Token) 의 유효기간 만료
     */
    @Test
    public void Access_Token_재발급_실패_Expired_Refresh_Token() throws Exception {
        // given
        final String expiredRefreshToken = jwtBuilder.expiredRefreshJwtBuild();

        // when
        final ResultActions resultActions = requestReIssue(expiredRefreshToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void Access_Token_재발급_실패_Invalid_Token() throws Exception {
        // given
        final String invalidRefreshToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestReIssue(invalidRefreshToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 요청 시, Cookie 에 유효하지 않은 JWT 정보를 추가함
     */
    @Test
    public void Access_Token_재발급_실패_Invalid_Refresh_Token() throws Exception {
        // given
        final String invalidRefreshToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestReIssue(invalidRefreshToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 최초 로그인 IP 주소와 요청 IP 주소가 일치하지 않음
     */
    @Test
    public void Access_Token_재발급_실패_IP_Address() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.build();

        final String response = requestLoginBasic(request).andReturn().getResponse().getContentAsString();

        final String refreshToken = objectMapper.readValue(response, TokenInfoResponse.class).refreshToken();

        // when
        final ResultActions resultActions = requestReIssueWithDifferentIpAddress(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_REFRESH_TOKEN, resultActions);
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 성공
     */
    @Test
    public void 로그아웃_성공() throws Exception {
        // given
        final LoginRequest request = LoginRequestBuilder.build();

        final String response = requestLoginBasic(request).andReturn().getResponse().getContentAsString();

        final String accessToken = objectMapper.readValue(response, TokenInfoResponse.class).accessToken();

        // when
        final ResultActions resultActions = requestLogout(accessToken);

        // then
        resultActions.andExpect(status().isOk());

        // 로그아웃 한 accessToken 을 활용한 접근
        // when
        final ResultActions resultActionsAfterLogout = requestLogout(accessToken);

        // then
        assertError(UserErrorCode.INVALID_ACCESS_TOKEN, resultActionsAfterLogout);
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 로그아웃_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestLogoutWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 로그아웃_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestLogout(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 로그아웃_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();

        // when
        final ResultActions resultActions = requestLogout(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 로그아웃_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestLogout(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 로그아웃_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestLogout(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    private ResultActions requestLoginBasic(LoginRequest request) throws Exception {
        return mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestLoginKakao(LoginKakaoRequest request) throws Exception {
        return mvc.perform(post("/user/login/kakao")
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
