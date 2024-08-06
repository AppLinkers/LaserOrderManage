package com.laser.ordermanage.user.integration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.cache.redis.dao.VerifyCode;
import com.laser.ordermanage.common.cache.redis.repository.VerifyCodeRedisRepository;
import com.laser.ordermanage.customer.dto.request.JoinBasicCustomerRequest;
import com.laser.ordermanage.customer.dto.request.JoinKakaoCustomerRequest;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.dto.request.JoinBasicCustomerRequestBuilder;
import com.laser.ordermanage.user.dto.request.JoinKakaoCustomerRequestBuilder;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequest;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequestBuilder;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponse;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponseBuilder;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserJoinIntegrationTest extends IntegrationTest {

    @Autowired
    private VerifyCodeRedisRepository verifyCodeRedisRepository;

    /**
     * 이메일 인증 코드 생성 및 이메일 전송 성공
     * - 신규 회원
     */
    @Test
    public void 이메일_인증코드_생성_및_이메일_전송_성공_신규회원() throws Exception {
        // given
        final String email = "new-user@gmail.com";
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildPossibleWithOutUserEntity();

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
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime expectedCreatedAt = LocalDateTime.parse("2023-10-02 10:20:30", formatter);

        // when
        final ResultActions resultActions = requestForRequestEmailVerify(user.getEmail());

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.registerModule(new JavaTimeModule()).readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithCreatedAt(actualResponse, expectedResponse, expectedCreatedAt);
    }

    /**
     * 이메일 인증 코드 검증 성공
     * - 신규 회원 및 인증 코드 검증 성공
     */
    @Test
    public void 이메일_인증코드_검증_성공_신규회원_및_인증코드_검증_성공() throws Exception {
        // given
        final String email = "new-user@gmail.com";
        이메일_인증코드_생성_및_이메일_전송_성공_신규회원();

        final Optional<VerifyCode> verifyCodeOptional = verifyCodeRedisRepository.findById(email);
        Assertions.assertThat(verifyCodeOptional.isPresent()).isTrue();
        final String verifyCode = verifyCodeOptional.get().getCode();

        final VerifyEmailRequest request = VerifyEmailRequest.builder()
                .email(email)
                .code(verifyCode)
                .build();

        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildPossibleWithOutUserEntity();

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
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime expectedCreatedAt = LocalDateTime.parse("2023-10-02 10:20:30", formatter);

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.registerModule(new JavaTimeModule()).readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithCreatedAt(actualResponse, expectedResponse, expectedCreatedAt);
    }

    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 요청 시, 이메일에 해당하는 인증 코드가 존재하지 않음
     */
    @Test
    public void 이메일_인증코드_검증_실패_인증코드_존재_X() throws Exception {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.unknownUserBuild();

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        assertError(UserErrorCode.NOT_FOUND_VERIFY_CODE, resultActions);
    }

    // TODO: 2024/04/01 invalidVerifyCode 생성 로직 변경
    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 인증 코드 검증 실패
     */
    @Test
    public void 이메일_인증코드_검증_실패_인증코드_검증() throws Exception {
        // given
        final String email = "new-user@gmail.com";
        이메일_인증코드_생성_및_이메일_전송_성공_신규회원();

        final Optional<VerifyCode> verifyCodeOptional = verifyCodeRedisRepository.findById(email);
        Assertions.assertThat(verifyCodeOptional.isPresent()).isTrue();
        final String verifyCode = verifyCodeOptional.get().getCode();

        final String invalidVerifyCode = StringUtils.reverse(verifyCode);
        if (verifyCode.equals(invalidVerifyCode)) {
            return;
        }

        final VerifyEmailRequest request = VerifyEmailRequest.builder()
                .email(email)
                .code(invalidVerifyCode)
                .build();

        // when
        final ResultActions resultActions = requestVerifyEmail(request);

        // then
        assertError(UserErrorCode.INVALID_VERIFY_CODE, resultActions);
    }

    /**
     * 고객 기본 회원가입 성공
     * - 신규 회원
     */
    @Test
    public void 고객_기본_회원가입_성공_신규회원() throws Exception {
        // given
        final JoinBasicCustomerRequest request = JoinBasicCustomerRequestBuilder.build();
        final UserEntity user = UserEntityBuilder.newUserBuild();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildCompletedWithUserEntity(user);

        // when
        final ResultActions resultActions = requestJoinBasicCustomer(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithOutCreatedAt(actualResponse, expectedResponse);
        Assertions.assertThat(actualResponse.createdAt()).isNotNull();
    }

    /**
     * 고객 기본 회원가입 성공
     * - 이메일 중복
     */
    @Test
    public void 고객_기본_회원가입_성공_이메일_중복() throws Exception {
        // given
        final JoinBasicCustomerRequest request = JoinBasicCustomerRequestBuilder.duplicateEmailBuild();
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildImpossibleWithUserEntity(user);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime expectedCreatedAt = LocalDateTime.parse("2023-10-02 10:20:30", formatter);

        // when
        final ResultActions resultActions = requestJoinBasicCustomer(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.registerModule(new JavaTimeModule()).readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithCreatedAt(actualResponse, expectedResponse, expectedCreatedAt);
    }

    /**
     * 고객 카카오 회원가입 성공
     * - 신규 회원
     */
    @Test
    public void 고객_카카오_회원가입_성공_신규회원() throws Exception {
        // given
        final JoinKakaoCustomerRequest request = JoinKakaoCustomerRequestBuilder.build();
        final UserEntity user = UserEntityBuilder.newUserBuild();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildCompletedWithUserEntity(user);

        // when
        final ResultActions resultActions = requestJoinKakaoCustomer(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithOutCreatedAt(actualResponse, expectedResponse);
        Assertions.assertThat(actualResponse.createdAt()).isNotNull();
    }

    /**
     * 고객 카카오 회원가입 성공
     * - 이메일 중복
     */
    @Test
    public void 고객_카카오_회원가입_성공_이메일_중복() throws Exception {
        // given
        final JoinKakaoCustomerRequest request = JoinKakaoCustomerRequestBuilder.duplicateEmailBuild();
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildImpossibleWithUserEntity(user);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime expectedCreatedAt = LocalDateTime.parse("2023-10-02 10:20:30", formatter);

        // when
        final ResultActions resultActions = requestJoinKakaoCustomer(request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UserJoinStatusResponse actualResponse = objectMapper.registerModule(new JavaTimeModule()).readValue(responseString, UserJoinStatusResponse.class);
        UserJoinStatusResponseBuilder.assertUserJoinStatusResponseWithCreatedAt(actualResponse, expectedResponse, expectedCreatedAt);
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

    private ResultActions requestJoinBasicCustomer(JoinBasicCustomerRequest request) throws Exception {
        return mvc.perform(post("/user/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestJoinKakaoCustomer(JoinKakaoCustomerRequest request) throws Exception {
        return mvc.perform(post("/user/kakao/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }
}
