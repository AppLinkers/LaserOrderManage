package com.laser.ordermanage.user.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.user.api.UserAccountAPI;
import com.laser.ordermanage.user.dto.request.RequestChangePasswordRequest;
import com.laser.ordermanage.user.dto.request.RequestChangePasswordRequestBuilder;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAccountAPI.class)
public class UserAccountAPIUnitTest extends APIUnitTest {


    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserAccountService userAccountService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 이메일 찾기 성공
     */
    @Test
    public void 이메일_찾기_성공() throws Exception {
        // given
        final String name = "사용자 이름 1";
        final String phone = "01011111111";
        final String email = "user1@gmail.com";

        // stub
        when(userAccountService.getUserEmail(any(), any())).thenReturn(
                new ListResponse<>(List.of(
                        GetUserEmailResponse.builder()
                                .name(name)
                                .email(email)
                                .build())
                )
        );

        // when
        final ResultActions resultActions = requestGetUserEmail(name, phone);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("contents").isArray())
                .andExpect(jsonPath("contents.size()").value(1))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("contents[0].name").value(name))
                .andExpect(jsonPath("contents[0].email").value(email));
    }

    /**
     * 이메일 찾기 실패
     * - 실패 사유 : 이름 필드 - null
     */
    @Test
    public void 이메일_찾기_실패_이름_필드_null() throws Exception{
        // given
        final String phone = "01011111111";

        // when
        final ResultActions resultActions = requestGetUserEmail(null, phone);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "name");
    }

    /**
     * 이메일 찾기 실패
     * - 실패 사유 : 이름 필드 - empty
     */
    @Test
    public void 이메일_찾기_실패_이름_필드_empty() throws Exception{
        // given
        final String emptyName = "";
        final String phone = "01011111111";

        // when
        final ResultActions resultActions = requestGetUserEmail(emptyName, phone);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "이름(상호)는 필수 입력값입니다.");
    }

    /**
     * 이메일 찾기 실패
     * - 실패 사유 : 이름 필드 유효성
     */
    @Test
    public void 이메일_찾기_실패_이름_필드_유효성() throws Exception{
        // given
        final String invalidName = "너어어어어어어어어무우우우우우우우우긴이름";
        final String phone = "01011111111";

        // when
        final ResultActions resultActions = requestGetUserEmail(invalidName, phone);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "이름(상호)의 최대 글자수는 20자입니다.");
    }

    /**
     * 이메일 찾기 실패
     * - 실패 사유 : 휴대폰 번호 필드 - null
     */
    @Test
    public void 이메일_찾기_실패_휴대폰_번호_필드_null() throws Exception {
        // given
        final String name = "사용자 이름 1";

        // when
        final ResultActions resultActions = requestGetUserEmail(name, null);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "phone");
    }

    /**
     * 이메일 찾기 실패
     * - 실패 사유 : 휴대폰 번호 필드 유효성
     */
    @Test
    public void 이메일_찾기_실패_휴대폰_번호_필드_유효성() throws Exception{
        // given
        final String name = "사용자 이름 1";
        final String emptyPhone = "";

        // when
        final ResultActions resultActions = requestGetUserEmail(name, emptyPhone);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 성공
     */
    @Test
    public void 비밀번호_찾기_이메일로_비밀번호_변경_링크_전송_성공() throws Exception {
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.build();

        // stub
        doNothing().when(userAccountService).requestChangePassword(any());

        // when
        final ResultActions resultActions = requestForRequestChangePasswordWithOutAuthentication(request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 이메일 필드 - null
     */
    @Test
    public void 비밀번호_찾기_이메일로_비밀번호_변경_링크_전송_실패_이메일_필드_null() throws Exception{
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.nullEmailBuild();

        // when
        final ResultActions resultActions = requestForRequestChangePasswordWithOutAuthentication(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일은 필수 입력값입니다.");
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 이메일 필드 유효성
     */
    @Test
    public void 비밀번호_찾기_이메일로_비밀번호_변경_링크_전송_실패_이메일_필드_유효성() throws Exception{
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.invalidEmailBuild();

        // when
        final ResultActions resultActions = requestForRequestChangePasswordWithOutAuthentication(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일 형식에 맞지 않습니다.");
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : base URL 필드 - null
     */
    @Test
    public void 비밀번호_찾기_이메일로_비밀번호_변경_링크_전송_실패_base_URL_필드_null() throws Exception{
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.nullBaseURLBuild();

        // when
        final ResultActions resultActions = requestForRequestChangePasswordWithOutAuthentication(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "base URL 은 필수 입력값입니다.");
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : base URL 필드 유효성
     */
    @Test
    public void 비밀번호_찾기_이메일로_비밀번호_변경_링크_전송_실패_base_URL_필드_유효성() throws Exception{
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.invalidBaseURLBuild();

        // when
        final ResultActions resultActions = requestForRequestChangePasswordWithOutAuthentication(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "base URL 형식이 유효하지 않습니다.");
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 성공
     */
    @Test
    @WithMockUser
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // stub
        doNothing().when(userAccountService).requestChangePassword(any());

        // when
        final ResultActions resultActions = requestForRequestChangePassword(accessToken, baseUrl);

        // then
        resultActions.andExpect(status().isOk());
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
        final ResultActions resultActions = requestForRequestChangePasswordWithoutAccessToken(baseUrl);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : base URL 필드 - null
     */
    @Test
    @WithMockUser
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_base_URL_필드_null() throws Exception{
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(accessToken, null);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "base-url");
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : base URL 필드 유효성
     */
    @Test
    @WithMockUser
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_base_URL_필드_유효성() throws Exception{
        // given
        final String accessToken = "access-token";
        final String invalidBaseUrl = "www.invalid.url.com";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(accessToken, invalidBaseUrl);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "base URL 형식이 유효하지 않습니다.");
    }

    private ResultActions requestGetUserEmail(String name, String phone) throws Exception {
        return mvc.perform(get("/user/email")
                        .param("name", name)
                        .param("phone", phone))
                .andDo(print());
    }

    private ResultActions requestForRequestChangePasswordWithOutAuthentication(RequestChangePasswordRequest request) throws Exception{
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

    private ResultActions requestForRequestChangePasswordWithoutAccessToken(String baseUrl) throws Exception {
        return mvc.perform(post("/user/password/email-link")
                        .param("base-url", baseUrl))
                .andDo(print());
    }

}
