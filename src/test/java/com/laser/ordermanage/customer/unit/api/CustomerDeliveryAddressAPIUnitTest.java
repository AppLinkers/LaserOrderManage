package com.laser.ordermanage.customer.unit.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.customer.api.CustomerDeliveryAddressAPI;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequestBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponseBuilder;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerDeliveryAddressAPI.class)
public class CustomerDeliveryAddressAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 고객 배송지 생성 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_배송지_생성_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 이름 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_이름_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullNameBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름은 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 이름 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_이름_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.emptyNameBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름은 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 이름 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_이름_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidNameBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름의 최대 글자수는 20자입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 우편번호 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_우편번호_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullZipCodeBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "우편번호는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 우편번호 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_우편번호_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidZipCodeBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "우편번호는 5자리 정수입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 기본 주소 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_기본주소_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullAddressBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "기본 주소는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 기본 주소 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_기본주소_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.emptyAddressBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "기본 주소는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 상세 주소 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_상세주소_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidDetailAddress();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상세 주소의 최대 글자수는 30자입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 수신자 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_수신자_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullReceiverBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 수신자 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_수신자_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.emptyReceiverBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 수신자 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_수신자_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidReceiverBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자의 최대 글자수는 10자입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 연락처 1 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_연락처1_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullPhone1Build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 연락처 1 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_연락처1_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidPhone1Build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 연락처 2 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_연락처2_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidPhone2Build();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 고객 배송지 생성 실패
     * - 실패 사유 : 배송지 기본 여부 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_생성_실패_배송지_기본여부_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullIsDefaultBuild();

        // when
        final ResultActions resultActions = requestCreateDeliveryAddress(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 기본 여부는 필수 사항입니다.");
    }

    /**
     * 고객 배송지 목록 조회 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final ListResponse<CustomerGetDeliveryAddressResponse> expectedResponse = new ListResponse<>(CustomerGetDeliveryAddressResponseBuilder.buildListOfCustomer1());

        // stub
        when(customerDeliveryAddressService.getDeliveryAddressList(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetDeliveryAddressList(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<CustomerGetDeliveryAddressResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<ListResponse<CustomerGetDeliveryAddressResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 고객 배송지 목록 조회 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_배송지_목록_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetDeliveryAddressList(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // stub
        doNothing().when(customerDeliveryAddressService).checkAuthorityCustomerOfDeliveryAddress(any(), any());
        doNothing().when(customerDeliveryAddressService).updateDeliveryAddress(any(), any(), any());

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_배송지_항목_수정_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : delivery-address-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_delivery_address_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidDeliveryAddressId = "invalid-address-id";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, invalidDeliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "delivery-address-id");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 이름 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_이름_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullNameBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름은 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 이름 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_이름_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.emptyNameBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름은 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 이름 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_이름_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidNameBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름의 최대 글자수는 20자입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 우편번호 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_우편번호_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullZipCodeBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "우편번호는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 우편번호 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_우편번호_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidZipCodeBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "우편번호는 5자리 정수입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 기본 주소 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_기본주소_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullAddressBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "기본 주소는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 기본 주소 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_기본주소_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.emptyAddressBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "기본 주소는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 상세 주소 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_상세주소_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidDetailAddress();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상세 주소의 최대 글자수는 30자입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 수신자 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_수신자_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullReceiverBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 수신자 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_수신자_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.emptyReceiverBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 수신자 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_수신자_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidReceiverBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자의 최대 글자수는 10자입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 연락처 1 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_연락처1_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullPhone1Build();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처는 필수 입력값입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 연락처 1 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_연락처1_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidPhone1Build();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 연락처 2 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_연락처2_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.invalidPhone2Build();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 배송지 기본 여부 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_배송지_기본여부_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.nullIsDefaultBuild();

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 기본 여부는 필수 사항입니다.");
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 배송지에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_배송지_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // stub
        doThrow(new CustomCommonException(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS)).when(customerDeliveryAddressService).checkAuthorityCustomerOfDeliveryAddress(any(), any());

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertError(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS, resultActions);
    }

    /**
     * 고객 배송지 항목 수정 실패
     * - 실패 사유 : 기본 배송지 해제
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_항목_수정_실패_기본_배송지_해제() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.createBuild();

        // stub
        doNothing().when(customerDeliveryAddressService).checkAuthorityCustomerOfDeliveryAddress(any(), any());
        doThrow(new CustomCommonException(CustomerErrorCode.DEFAULT_DELIVERY_ADDRESS_DELETE)).when(customerDeliveryAddressService).updateDeliveryAddress(any(), any(), any());

        // when
        final ResultActions resultActions = requestUpdateDeliveryAddress(accessToken, deliveryAddressId, request);

        // then
        assertError(CustomerErrorCode.DEFAULT_DELIVERY_ADDRESS_DELETE, resultActions);
    }

    /**
     * 고객 배송지 삭제 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_삭제_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";

        // stub
        doNothing().when(customerDeliveryAddressService).checkAuthorityCustomerOfDeliveryAddress(any(), any());
        doNothing().when(customerDeliveryAddressService).deleteDeliveryAddress(any());

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(accessToken, deliveryAddressId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_배송지_삭제_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(accessToken, deliveryAddressId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : delivery-address-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_삭제_실패_delivery_address_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidDeliveryAddressId = "invalid-address-id";

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(accessToken, invalidDeliveryAddressId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "delivery-address-id");
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 배송지에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_삭제_실패_배송지_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";

        // stub
        doThrow(new CustomCommonException(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS)).when(customerDeliveryAddressService).checkAuthorityCustomerOfDeliveryAddress(any(), any());

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(accessToken, deliveryAddressId);

        // then
        assertError(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS, resultActions);
    }

    /**
     * 고객 배송지 삭제 실패
     * - 실패 사유 : 기본 배송지 삭제
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_배송지_삭제_실패_기본_배송지_삭제() throws Exception {
        // given
        final String accessToken = "access-token";
        final String deliveryAddressId = "1";

        // stub
        doNothing().when(customerDeliveryAddressService).checkAuthorityCustomerOfDeliveryAddress(any(), any());
        doThrow(new CustomCommonException(CustomerErrorCode.DEFAULT_DELIVERY_ADDRESS_DELETE)).when(customerDeliveryAddressService).deleteDeliveryAddress(any());

        // when
        final ResultActions resultActions = requestDeleteDeliveryAddress(accessToken, deliveryAddressId);

        // then
        assertError(CustomerErrorCode.DEFAULT_DELIVERY_ADDRESS_DELETE, resultActions);
    }

    private ResultActions requestCreateDeliveryAddress(String accessToken, CustomerCreateOrUpdateDeliveryAddressRequest request) throws Exception {
        return mvc.perform(post("/customer/delivery-address")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestGetDeliveryAddressList(String accessToken) throws Exception {
        return mvc.perform(get("/customer/delivery-address")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestUpdateDeliveryAddress(String accessToken, String deliveryAddressId, CustomerCreateOrUpdateDeliveryAddressRequest request) throws Exception {
        return mvc.perform(put("/customer/delivery-address/{delivery-address-id}", deliveryAddressId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteDeliveryAddress(String accessToken, String deliveryAddressId) throws Exception {
        return mvc.perform(delete("/customer/delivery-address/{delivery-address-id}", deliveryAddressId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
