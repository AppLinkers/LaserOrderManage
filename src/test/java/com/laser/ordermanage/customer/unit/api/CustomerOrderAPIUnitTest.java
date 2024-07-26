package com.laser.ordermanage.customer.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.api.CustomerOrderAPI;
import com.laser.ordermanage.customer.dto.request.*;
import com.laser.ordermanage.customer.dto.response.CustomerCreateDrawingResponse;
import com.laser.ordermanage.customer.dto.response.CustomerCreateDrawingResponseBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponse;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
import com.laser.ordermanage.customer.service.CustomerOrderEmailService;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import com.laser.ordermanage.order.domain.*;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.service.OrderService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerOrderAPI.class)
public class CustomerOrderAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerOrderService customerOrderService;

    @MockBean
    private CustomerOrderEmailService customerOrderEmailService;

    @MockBean
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 고객 거래 생성 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_거래_생성_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 거래 이름 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_이름_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullNameBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "거래 이름은 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 거래 이름 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_이름_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.emptyNameBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "거래 이름은 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 거래 이름 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_이름_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidNameBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "거래 이름의 최대 글자수는 20자입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 도면 목록 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_도면_목록_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullDrawingListBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "도면은 최소한 한개 이상이어야 합니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 도면 목록 필드 비어있는 리스트
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_도면_목록_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.emptyDrawingListBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "도면은 최소한 한개 이상이어야 합니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 도면 수량 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_도면_수량_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullCountDrawingBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수량은 필수 입력값입니다.");
    }


    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 도면 수량 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_도면_수량_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidCountDrawingBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수량은 1 이상, 100 이하의 정수 입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 도면 재료 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_도면_재료_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullIngredientDrawingBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재료 선택은 필수 사항입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 도면 재료 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_도면_재료_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.emptyIngredientDrawingBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재료 선택은 필수 사항입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 도면 두께 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_도면_두께_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullThicknessDrawingBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "두께는 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 도면 두께 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_도면_두께_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidThicknessDrawingBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "두께는 1 이상, 19 이하의 정수 입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 이름 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_이름_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullNameDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름은 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 이름 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_이름_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.emptyNameDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름은 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 이름 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_이름_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidNameDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "배송지 이름의 최대 글자수는 20자입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 우편번호 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_우편번호_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullZipCodeDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "우편번호는 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 우편번호 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_우편번호_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidZipCodeDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "우편번호는 5자리 정수입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 기본 주소 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_기본주소_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullAddressDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "기본 주소는 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 기본 주소 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_기본주소_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.emptyAddressDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "기본 주소는 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 상세 주소 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_기본주소_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidDetailAddressDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상세 주소의 최대 글자수는 30자입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 수신자 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_수신자_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullReceiverDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자는 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 수신자 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_수신자_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.emptyReceiverDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자는 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 수신자 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_수신자_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidReceiverDeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수신자의 최대 글자수는 10자입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 연락처 1 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_연락처1_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullPhone1DeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처는 필수 입력값입니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 연락처 1 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_연락처1_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidPhone1DeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 배송지 연락처 2 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_배송지_연락처2_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.invalidPhone2DeliveryAddressBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 고객 거래 생성 실패
     * - 실패 사유 : 신규 발급 유무 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_거래_생성_실패_신규발급_유무_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.nullNewIssueBuild();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "신규 발급 유무는 필수 사항입니다.");
    }

    /**
     * 거래 배송지 수정 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래의_배송지_수정_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, orderId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래의_배송지_수정_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, orderId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래의_배송지_수정_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, invalidOrderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 주소지 Id 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래의_배송지_수정_실패_주소지_id_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.nullDeliveryAddressIdBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "주소지 선택은 필수 사항입니다.");
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래의_배송지_수정_실패_거래_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(customerOrderService).checkAuthorityOfOrder(any(), any());

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, orderId, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 배송지에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래의_배송지_수정_실패_배송지_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // stub
        doThrow(new CustomCommonException(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS)).when(customerDeliveryAddressService).checkAuthorityCustomerOfDeliveryAddress(any(), any());

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, orderId, request);

        // then
        assertError(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 거래의 단계가 배송지 변경 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래의_배송지_수정_실패_거래배송지수정_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(customerOrderService).updateOrderDeliveryAddress(any(), any());

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, orderId, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 도면 항목 추가 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();
        final Long drawingId = 18L;
        final CustomerCreateDrawingResponse expectedResponse = CustomerCreateDrawingResponseBuilder.build();

        // stub
        when(customerOrderService.createOrderDrawing(any(), any())).thenReturn(drawingId);

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final CustomerCreateDrawingResponse actualResponse = objectMapper.readValue(responseString, CustomerCreateDrawingResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_도면_항목_추가_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, invalidOrderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 수량 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_수량_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.nullCountBuild();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수량은 필수 입력값입니다.");
    }


    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 수량 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_수량_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.invalidCountBuild();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수량은 1 이상, 100 이하의 정수 입니다.");
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 재료 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_재료_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.nullIngredientBuild();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재료 선택은 필수 사항입니다.");
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 재료 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_재료_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.emptyIngredientBuild();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재료 선택은 필수 사항입니다.");
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 두께 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_두께_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.nullThicknessBuild();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "두께는 필수 입력값입니다.");
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 두께 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_두께_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.invalidThicknessBuild();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "두께는 1 이상, 19 이하의 정수 입니다.");
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_거래_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(customerOrderService).checkAuthorityOfOrder(any(), any());

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 거래 단계가 도면 항목 추가 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_추가_실패_거래도면추가_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(customerOrderService).createOrderDrawing(any(), any());

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 도면 항목 수정 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_도면_항목_수정_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, invalidOrderId, drawingId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : drawing-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_drawing_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String invalidDrawingId = "invalid-drawing-id";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, invalidDrawingId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "drawing-id");
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 수량 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_수량_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.nullCountBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수량은 필수 입력값입니다.");
    }


    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 수량 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_수량_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.invalidCountBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "수량은 1 이상, 100 이하의 정수 입니다.");
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 재료 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_재료_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.nullIngredientBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재료 선택은 필수 사항입니다.");
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 재료 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_재료_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.emptyIngredientBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "재료 선택은 필수 사항입니다.");
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 두께 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_두께_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.nullThicknessBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "두께는 필수 입력값입니다.");
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 두께 필드 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_두께_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.invalidThicknessBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "두께는 1 이상, 19 이하의 정수 입니다.");
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_거래_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(customerOrderService).checkAuthorityOfOrder(any(), any());

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 거래 단계가 도면 항목 수정 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_항목_수정_실패_거래도면항목수정_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(customerOrderService).updateOrderDrawing(any(), any(), any());

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 도면 삭제 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_삭제_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, drawingId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_도면_삭제_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, drawingId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_삭제_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final String drawingId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, invalidOrderId, drawingId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : drawing-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_삭제_실패_drawing_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String invalidDrawingId = "invalid-drawing-id";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, invalidDrawingId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "drawing-id");
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_삭제_실패_거래_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(customerOrderService).checkAuthorityOfOrder(any(), any());

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, drawingId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 거래 단계가 도면 삭제 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_삭제_실패_도면삭제_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(customerOrderService).deleteOrderDrawing(any(), any());

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, drawingId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 거래 마지막 도면을 삭제할 수 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_도면_삭제_실패_마지막도면_삭제() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String drawingId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.LAST_DRAWING_DELETE)).when(customerOrderService).deleteOrderDrawing(any(), any());

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, drawingId);

        // then
        assertError(OrderErrorCode.LAST_DRAWING_DELETE, resultActions);
    }

    /**
     * 거래 견적서 승인 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_견적서_승인_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, orderId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_견적서_승인_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_견적서_승인_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_견적서_승인_실패_거래_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(customerOrderService).checkAuthorityOfOrder(any(), any());

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 거래 단계가 견적서 승인 가능 단계(견적 대기)가 아님
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_견적서_승인_실패_견적서승인_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(customerOrderService).approveQuotation(any());

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, orderId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 거래 견적서가 존재하지 않음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_견적서거래_견적서_승인_실패_견적서_존재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.NOT_FOUND_QUOTATION)).when(customerOrderService).approveQuotation(any());

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, orderId);

        // then
        assertError(OrderErrorCode.NOT_FOUND_QUOTATION, resultActions);
    }

    /**
     * 거래 발주서 작성 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();

        final CustomerCreateOrUpdateOrderPurchaseOrderResponse expectedResponse = CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder.createBuild();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);
        when(customerOrderService.createOrderPurchaseOrder(any(), any(), any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final CustomerCreateOrUpdateOrderPurchaseOrderResponse actualResponse = objectMapper.readValue(responseString, CustomerCreateOrUpdateOrderPurchaseOrderResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 발주서 수정 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_수정_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.updateBuild();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);

        final CustomerCreateOrUpdateOrderPurchaseOrderResponse expectedResponse = CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder.updateBuild();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);
        when(customerOrderService.updateOrderPurchaseOrder(any(), any(), any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final CustomerCreateOrUpdateOrderPurchaseOrderResponse actualResponse = objectMapper.readValue(responseString, CustomerCreateOrUpdateOrderPurchaseOrderResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_발주서_작성_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 발수저 작성 및 수정 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, invalidOrderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 발수저 작성 및 수정 실패
     * - 실패 사유 : 검수 기간 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_검수기간_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.nullInspectionPeriodBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "검수 기간은 필수 입력값입니다.");
    }

    /**
     * 거래 발수저 작성 및 수정 실패
     * - 실패 사유 : 검수 조건 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_검수조건_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.nullInspectionConditionBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "검수 조건은 필수 입력값입니다.");
    }

    /**
     * 거래 발수저 작성 및 수정 실패
     * - 실패 사유 : 검수 조건 필드 empty
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_검수조건_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.emptyInspectionConditionBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "검수 조건은 필수 입력값입니다.");
    }

    /**
     * 거래 발수저 작성 및 수정 실패
     * - 실패 사유 : 지급 일자 필드 null
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_지급일자_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.nullPaymentDateBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "지급 일자는 필수 입력값입니다.");
    }

    /**
     * 거래 발수저 작성 및 수정 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_거래_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(customerOrderService).checkAuthorityOfOrder(any(), any());

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 발수저 작성 및 수정 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_존재하지_않는_거래() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.NOT_FOUND_ORDER)).when(orderService).getOrderById(any());

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertError(OrderErrorCode.NOT_FOUND_ORDER, resultActions);
    }

    /**
     * 거래 발수저 작성 및 수정 실패
     * - 실패 사유 : 거래 단계가 발주서 작성 및 수정 가능 단계(견적 승인)가 아님
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_발주서작성및수정_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);
        order.approvePurchaseOrder();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, order.getStage().getValue());
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 발주서의 검수기간이 거래 납기일 이전임
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_검수기간_거래_납기일_이전() throws Exception {
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.earlyInspectionPeriodBuild();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "발주서의 검수기간 및 지급일은 거래 납기일 이후이어야 합니다.");
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 발주서의 지급일이 거래 납기일 이전임
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_및_수정_실패_지급일_거래_납기일_이전() throws Exception {
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.earlyPaymentDateBuild();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "발주서의 검수기간 및 지급일은 거래 납기일 이후이어야 합니다.");
    }

    /**
     * 거래 발수저 작성 실패
     * - 실패 사유 : 발주서 작성 시, 요청에 발주서의 파일이 존재하지 않음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_작성_실패_발주서_파일_존재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);
        doThrow(new CustomCommonException(OrderErrorCode.REQUIRED_PURCHASE_ORDER_FILE)).when(customerOrderService).createOrderPurchaseOrder(any(), any(), any());

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrderWithOutFile(accessToken, orderId, request);

        // then
        assertError(OrderErrorCode.REQUIRED_PURCHASE_ORDER_FILE, resultActions);
    }


    private ResultActions requestCreateOrder(String accessToken, CustomerCreateOrderRequest request) throws Exception {
        return mvc.perform(post("/customer/order")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateOrderDeliveryAddress(String accessToken, String orderId, CustomerUpdateOrderDeliveryAddressRequest request) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/delivery-address", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestCreateOrderDrawing(String accessToken, String orderId, CustomerCreateDrawingRequest request) throws Exception {
        return mvc.perform(post("/customer/order/{order-id}/drawing", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateOrderDrawing(String accessToken, String orderId, String drawingId, CustomerUpdateDrawingRequest request) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/drawing/{drawing-id}", orderId, drawingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteOrderDrawing(String accessToken, String orderId, String drawingId) throws Exception {
        return mvc.perform(delete("/customer/order/{order-id}/drawing/{drawing-id}", orderId, drawingId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestApproveQuotation(String accessToken, String orderId) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/quotation", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderPurchaseOrder(String accessToken, String orderId, MockMultipartFile file, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/customer/order/{order-id}/purchase-order", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile purchaseOrder = new MockMultipartFile("purchaseOrder", "purchaseOrder", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(purchaseOrder)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderPurchaseOrderWithOutFile(String accessToken, String orderId, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/customer/order/{order-id}/purchase-order", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile purchaseOrder = new MockMultipartFile("purchaseOrder", "purchaseOrder", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(purchaseOrder)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
