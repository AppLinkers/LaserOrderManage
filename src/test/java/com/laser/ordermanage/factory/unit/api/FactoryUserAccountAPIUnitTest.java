package com.laser.ordermanage.factory.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.factory.api.FactoryUserAccountAPI;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequest;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequestBuilder;
import com.laser.ordermanage.factory.dto.response.FactoryGetFactoryAccountResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetFactoryAccountResponseBuilder;
import com.laser.ordermanage.factory.exception.FactoryErrorCode;
import com.laser.ordermanage.factory.service.FactoryUserAccountService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FactoryUserAccountAPI.class)
public class FactoryUserAccountAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private FactoryUserAccountService factoryUserAccountService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 공장 정보 조회 성공
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 공장_정보_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryGetFactoryAccountResponse expectedResponse = FactoryGetFactoryAccountResponseBuilder.build();

        // stub
        when(factoryUserAccountService.getFactoryAccount(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetFactoryAccount(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryGetFactoryAccountResponse actualResponse = objectMapper.readValue(responseString, FactoryGetFactoryAccountResponse.class);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 공장 정보 조회 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 공장_정보_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetFactoryAccount(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 공장 정보 조회 실패
     * - 실패 사유 : 이메일에 해당하는 공장 정보가 존재하지 않음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 공장_정보_조회_실패_공장_정보_존재() throws Exception {
        // given
        final String accessToken = "access-token";

        // stub
        when(factoryUserAccountService.getFactoryAccount(any())).thenThrow(new CustomCommonException(FactoryErrorCode.NOT_FOUND_FACTORY));

        // when
        final ResultActions resultActions = requestGetFactoryAccount(accessToken);

        // then
        assertError(FactoryErrorCode.NOT_FOUND_FACTORY, resultActions);
    }


    /**
     * 공장 정보 변경 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 공장_정보_변경_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 공장_정보_변경_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 공장 상호 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_상호_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.nullCompanyNameBuild();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상호는 필수 입력값입니다.");
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 공장 상호 필드 empty
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_상호_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.emptyCompanyNameBuild();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상호는 필수 입력값입니다.");
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 공장 상호 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_상호_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.invalidCompanyNameBuild();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상호의 최대 글자수는 20자입니다.");
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 공장 대표자 이름 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_대표자_이름_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.nullCompanyRepresentativeBuild();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "대표자 이름은 필수 입력값입니다.");
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 공장 대표자 이름 필드 empty
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_대표자_이름_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.emptyCompanyRepresentativeBuild();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "대표자 이름은 필수 입력값입니다.");
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 공장 대표자 이름 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_대표자_이름_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.invalidCompanyRepresentativeBuild();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "대표자 이름의 최대 글자수는 10자입니다.");
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 공장 Fax 번호 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_Fax_번호_필드_null() throws Exception  {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.nullCompanyFaxBuild();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "FAX 번호는 필수 입력값입니다.");
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 공장 Fax 번호 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_Fax_번호_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.invalidCompanyFaxBuild();

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "FAX 번호 형식에 맞지 않습니다.");
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 이메일에 해당하는 공장 정보가 존재하지 않음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 공장_정보_변경_실패_공장_정보_존재() throws Exception {
        // given
        final String accessToken = "access-token";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(FactoryErrorCode.NOT_FOUND_FACTORY)).when(factoryUserAccountService).updateFactoryAccount(any(), any());

        // when
        final ResultActions resultActions = requestUpdateFactoryAccount(accessToken, request);

        // then
        assertError(FactoryErrorCode.NOT_FOUND_FACTORY, resultActions);
    }

    private ResultActions requestGetFactoryAccount(String accessToken) throws Exception {
        return mvc.perform(get("/factory/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestUpdateFactoryAccount(String accessToken, FactoryUpdateFactoryAccountRequest request) throws Exception {
        return mvc.perform(patch("/factory/user")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }
}
