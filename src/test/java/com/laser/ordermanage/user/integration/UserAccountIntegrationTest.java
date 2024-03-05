package com.laser.ordermanage.user.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.user.dto.request.*;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        final String customerPhone = "01011111111";

        // when
        final ResultActions resultActions = requestGetUserEmail(customerName, customerPhone);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("contents").isArray())
                .andExpect(jsonPath("contents.size()").value(2))
                .andExpect(jsonPath("totalElements").value(2))
                .andExpect(jsonPath("contents[0].name").value(customerName))
                .andExpect(jsonPath("contents[1].name").value(customerName));

    }

    /**
     * 공장 이메일 찾기 성공
     */
    @Test
    public void 공장_이메일_찾기_성공() throws Exception {
        // given
        final String factoryName = "금오 M.T";
        final String factoryPhone = "01011111111";

        // when
        final ResultActions resultActions = requestGetUserEmail(factoryName, factoryPhone);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("contents").isArray())
                .andExpect(jsonPath("contents.size()").value(1))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("contents[0].name").value(factoryName));

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
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("contents").isArray())
                .andExpect(jsonPath("contents.size()").value(0))
                .andExpect(jsonPath("totalElements").value(0));

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
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 성공
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String baseUrl = "https://www.kumoh.org/edit-password";

        // when
        final ResultActions resultActions = requestForRequestChangePassword(accessToken, baseUrl);

        // then
        resultActions.andExpect(status().isOk());

    }

    /**
     * 비밀번호 변경 - 이메일로 비밀번호 변경 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 에 해당하는 사용자가 존재하지 않음
     */
    @Test
    public void 비밀번호_변경_이메일로_비밀번호_변경_링크_전송_실패_사용자_존재() throws Exception{
        // given
        final String accessTokenOfUnknownUser = jwtBuilder.accessJwtOfUnknownUserBuild();
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
        final String changePasswordToken = jwtBuilder.changePasswordJwtBuild();
        final LoginRequest expectedLoginRequest = LoginRequestBuilder.newPasswordBuild();
        final ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password(expectedLoginRequest.password())
                .build();

        // given
        ResultActions resultActions = requestChangePassword(changePasswordToken, request);

        // then
        resultActions.andExpect(status().isOk());

        // then - 새로운 비밀번호로 로그인
        ResultActions resultActionsOfLogin = requestLogin(expectedLoginRequest);
        resultActionsOfLogin.andExpect(status().isOk());

    }

    /**
     * 비밀번호 변경 실패
     * 실패 사유 : 요청 시, Header 에 Authorization 정보 (Change Password Token) 를 추가하지 않음
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
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가함
     */
    @Test
    public void 비밀번호_변경_실패_Token_Type() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        ResultActions resultActions = requestChangePassword(accessToken, request);

        // then
        assertError(UserErrorCode.INVALID_CHANGE_PASSWORD_TOKEN, resultActions);

    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 비어있는 Authorization 정보를 추가함
     */
    @Test
    public void 비밀번호_변경_실패_Empty_Change_Password_Token() throws Exception{
        // given
        final String emptyChangePasswordToken = "";
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        ResultActions resultActions = requestChangePassword(emptyChangePasswordToken, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);

    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 유효하지 않은 Authorization 정보를 추가함
     */
    @Test
    public void 비밀번호_변경_실패_Invalid_Change_Password_Token() throws Exception{
        // given
        final String invalidChangePasswordToken = jwtBuilder.invalidJwtBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        ResultActions resultActions = requestChangePassword(invalidChangePasswordToken, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);

    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Change Password Token) 의 유효기간 만료
     */
    @Test
    public void 비밀번호_변경_실패_Expired_Change_Password_Token() throws Exception{
        // given
        final String expiredChangePasswordToken = jwtBuilder.expiredChangePasswordJwtBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        ResultActions resultActions = requestChangePassword(expiredChangePasswordToken, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);

    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 에 권한 정보가 없음
     */
    @Test
    public void 비밀번호_변경_실패_Unauthorized_Change_Password_Token() throws Exception{
        // given
        final String unauthorizedChangePasswordToken = jwtBuilder.unauthorizedChangePasswordJwtBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        ResultActions resultActions = requestChangePassword(unauthorizedChangePasswordToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);

    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Change Password Token) 에 해당하는 사용자가 존재하지 않음
     */
    @Test
    public void 비밀번호_변경_실패_사용자_존재() throws Exception{
        // given
        final String changePasswordTokenOfUnknownUser = jwtBuilder.changePasswordJwtOfUnknownUserBuild();
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        // when
        ResultActions resultActions = requestChangePassword(changePasswordTokenOfUnknownUser, request);

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
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

    private ResultActions requestChangePassword(String changePasswordToken, ChangePasswordRequest request) throws Exception {
        return mvc.perform(patch("/user/password")
                        .header("Authorization", "Bearer " + changePasswordToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestChangePasswordWithOutChangePasswordToken(ChangePasswordRequest request) throws Exception{
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
}
