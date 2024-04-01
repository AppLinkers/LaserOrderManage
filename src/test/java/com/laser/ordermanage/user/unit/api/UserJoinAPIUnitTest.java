package com.laser.ordermanage.user.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.dto.request.JoinCustomerRequest;
import com.laser.ordermanage.user.api.UserJoinAPI;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.dto.request.JoinCustomerRequestBuilder;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequest;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequestBuilder;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponse;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponseBuilder;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.service.UserJoinService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserJoinAPI.class)
public class UserJoinAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserJoinService userJoinService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 이메일 인증 코드 생성 및 이메일 전송 성공
     * - 신규 회원
     */
    @Test
    public void 이메일_인증코드_생성_및_이메일_전송_성공_신규회원() throws Exception {
        // given
        final String email = "new-user@gmail.com";
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildPossibleWithOutUserEntity();

        // stub
        when(userJoinService.requestEmailVerify(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestForRequestEmailVerify(email);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.readValue(responseString, UserJoinStatusResponse.class);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 이메일 인증 코드 생성 및 이메일 전송 성공
     * - 이메일 중복
     */
    @Test
    public void 이메일_인증코드_생성_및_이메일_전송_성공_이메일_중복() throws Exception {
        // given
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildImpossibleWithUserEntity(user);

        // stub
        when(userJoinService.requestEmailVerify(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestForRequestEmailVerify(user.getEmail());

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithOutCreatedAt(actualResponse, expectedResponse);
    }

    /**
     * 이메일 인증 코드 생성 및 이메일 전송 실패
     * - 실패 사유 : 이메일 파라미터 null
     */
    @Test
    public void 이메일_인증코드_생성_및_이메일_전송_실패_이메일_파라미터_null() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestForRequestEmailVerify(null);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "email");
    }

    /**
     * 이메일 인증 코드 생성 및 이메일 전송 실패
     * - 실패 사유 : 이메일 파라미터 유효성
     */
    @Test
    public void 이메일_인증코드_생성_및_이메일_전송_실패_이메일_파라미터_유효성() throws Exception {
        // given
        final String invalidEmail = ".user.name@domain.com";

        // when
        final ResultActions resultActions = requestForRequestEmailVerify(invalidEmail);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "이메일 형식에 맞지 않습니다.");
    }

    /**
     * 이메일 인증 코드 검증 성공
     * - 신규 회원 및 인증 코드 검증 성공
     */
    @Test
    public void 이메일_인증코드_검증_성공_신규회원_및_인증코드_검증_성공() throws Exception {
        // final
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildPossibleWithOutUserEntity();

        // stub
        when(userJoinService.verifyEmail(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.readValue(responseString, UserJoinStatusResponse.class);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 이메일 인증 코드 검증 성공
     * - 이메일 중복
     */
    @Test
    public void 이메일_인증코드_검증_성공_이메일_중복() throws Exception {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.duplicatedUserBuild();
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildImpossibleWithUserEntity(user);

        // stub
        when(userJoinService.verifyEmail(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithOutCreatedAt(actualResponse, expectedResponse);
    }

    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 이메일 필드 null
     */
    @Test
    public void 이메일_인증코드_검증_실패_이메일_필드_null() throws Exception {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.nullEmailBuild();

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일은 필수 입력값입니다.");
    }

    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 이메일 필드 유효성
     */
    @Test
    public void 이메일_인증코드_검증_실패_이메일_필드_유효성() throws Exception {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.invalidEmailBuild();

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일 형식에 맞지 않습니다.");
    }

    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 인증코드 필드 null
     */
    @Test
    public void 이메일_인증코드_검증_실패_인증코드_필드_null() throws Exception {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.nullCodeBuild();

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일 인증코드는 필수 입력값입니다.");
    }

    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 인증코드 필드 유효성
     */
    @Test
    public void 이메일_인증코드_검증_실패_인증코드_필드_유효성() throws Exception {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.invalidCodeBuild();

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일 인증코드는 6자리 정수입니다.");
    }

    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 요청 시, 이메일에 해당하는 인증 코드가 존재하지 않음
     */
    @Test
    public void 이메일_인증코드_검증_실패_인증코드_존재_X() throws Exception{
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.unknownUserBuild();

        // stub
        doThrow(new CustomCommonException(UserErrorCode.NOT_FOUND_VERIFY_CODE)).when(userJoinService).verifyEmail(any());

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        assertError(UserErrorCode.NOT_FOUND_VERIFY_CODE, resultActions);
    }


    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 인증 코드 검증 실패
     */
    @Test
    public void 이메일_인증코드_검증_실패_인증코드_검증() throws Exception {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(UserErrorCode.INVALID_VERIFY_CODE)).when(userJoinService).verifyEmail(any());

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        assertError(UserErrorCode.INVALID_VERIFY_CODE, resultActions);
    }

    /**
     * 고객 회원가입 성공
     * - 신규 회원
     */
    @Test
    public void 고객_회원가입_성공_신규회원() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.build();
        final UserEntity user = UserEntityBuilder.newUserBuild();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildCompletedWithUserEntity(user);

        // stub
        when(userJoinService.joinCustomer(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithOutCreatedAt(actualResponse, expectedResponse);
    }

    /**
     * 고객 회원가입 성공
     * - 이메일 중복
     */
    @Test
    public void 고객_회원가입_성공_이메일_중복() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.duplicateEmailBuild();
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildImpossibleWithUserEntity(user);

        // stub
        when(userJoinService.joinCustomer(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithOutCreatedAt(actualResponse, expectedResponse);
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 이메일 필드 null
     */
    @Test
    public void 고객_회원가입_실패_이메일_필드_null() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.nullEmailBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일은 필수 입력값입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 이메일 필드 유효성
     */
    @Test
    public void 고객_회원가입_실패_이메일_필드_유효성() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.invalidEmailBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이메일 형식에 맞지 않습니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 비밀번호 필드 null
     */
    @Test
    public void 고객_회원가입_실패_비밀번호_필드_null() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.nullPasswordBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "비밀번호는 필수 입력값입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 비밀번호 필드 유효성
     */
    @Test
    public void 고객_회원가입_실패_비밀번호_필드_유효성() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.invalidPasswordBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "비밀번호는 8 자리 이상 영문, 숫자, 특수문자를 사용하세요.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 이름 필드 null
     */
    @Test
    public void 고객_회원가입_실패_이름_필드_null() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.nullNameBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이름은 필수 입력값입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 이름 필드 empty
     */
    @Test
    public void 고객_회원가입_실패_이름_필드_empty() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.emptyNameBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이름은 필수 입력값입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 이름 필드 유효성
     */
    @Test
    public void 고객_회원가입_실패_이름_필드_유효성() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.invalidNameBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "이름의 최대 글자수는 10자입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 회사 이름 필드 유효성
     */
    @Test
    public void 고객_회원가입_실패_회사이름_필드_유효성() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.invalidCompanyNameBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "회사 이름의 최대 글자수는 20자입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 연락처 null
     */
    @Test
    public void 고객_회원가입_실패_연락처_필드_null() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.nullPhoneBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처는 필수 입력값입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 연락처 유효성
     */
    @Test
    public void 고객_회원가입_실패_연락처_필드_유효성() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.invalidPhoneBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 우편번호 null
     */
    @Test
    public void 고객_회원가입_실패_우편번호_필드_null() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.nullZipCodeBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "우편번호는 필수 입력값입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 우편번호 유효성
     */
    @Test
    public void 고객_회원가입_실패_우편번호_필드_유효성() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.invalidZipCodeBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "우편번호는 5자리 정수입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 기본 주소 null
     */
    @Test
    public void 고객_회원가입_실패_기본주소_필드_null() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.nullAddressBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "기본 주소는 필수 입력값입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 기본 주소 empty
     */
    @Test
    public void 고객_회원가입_실패_기본주소_필드_empty() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.emptyAddressBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "기본 주소는 필수 입력값입니다.");
    }

    /**
     * 고객 회원가입 실패
     * - 실패 사유 : 상세주소 유효성
     */
    @Test
    public void 고객_회원가입_실패_상세주소_필드_유효성() throws Exception {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.invalidDetailAddressBuild();

        // when
        final ResultActions resultActions = requestJoinCustomer(request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상세 주소의 최대 글자수는 30자입니다.");
    }

    private ResultActions requestForRequestEmailVerify(String email) throws Exception {
        return mvc.perform(post("/user/request-verify")
                        .param("email", email))
                .andDo(print());
    }

    private ResultActions requestVerifyEmail(VerifyEmailRequest request) throws Exception {
        return mvc.perform(post("/user/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestJoinCustomer(JoinCustomerRequest request) throws Exception {
        return mvc.perform(post("/user/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }
}
