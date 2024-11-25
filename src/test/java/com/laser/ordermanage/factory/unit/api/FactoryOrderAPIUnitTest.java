package com.laser.ordermanage.factory.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.scheduler.service.ScheduleService;
import com.laser.ordermanage.factory.api.FactoryOrderAPI;
import com.laser.ordermanage.factory.dto.request.*;
import com.laser.ordermanage.factory.dto.response.*;
import com.laser.ordermanage.factory.service.FactoryOrderEmailService;
import com.laser.ordermanage.factory.service.FactoryOrderService;
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

@WebMvcTest(FactoryOrderAPI.class)
public class FactoryOrderAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private FactoryOrderService factoryOrderService;

    @MockBean
    private FactoryOrderEmailService factoryOrderEmailService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ScheduleService scheduleService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 거래 긴급 설정 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_긴급_설정_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.isUrgentTrueBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(accessToken, orderId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 긴급 설정 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_긴급_설정_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.isUrgentTrueBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(accessToken, orderId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 긴급 설정 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_긴급_설정_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.isUrgentTrueBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(accessToken, orderId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 긴급 설정 실패
     * - 실패 사유 : 거래 긴급 유무 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_긴급_설정_실패_긴급_유무_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.nullIsUrgentBuild();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "거래 긴급 유무는 필수 사항입니다.");
    }

    /**
     * 거래 긴급 설정 실패
     * - 실패 사유 : 거래 단계가 거래 긴급 설정 가능 단계(견적 대기, 견적 승인, 제작 중, 제작 완료)가 아님
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_긴급_설정_실패_거래긴급설정_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.isUrgentTrueBuild();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(factoryOrderService).updateOrderIsUrgent(any(), any());

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(accessToken, orderId, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 견적서 작성 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_작성_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        final Order order = OrderBuilder.build();

        final FactoryCreateOrUpdateOrderQuotationResponse expectedResponse = FactoryCreateOrUpdateOrderQuotationResponseBuilder.createBuild();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);
        when(factoryOrderService.createOrderQuotation(any(), any(), any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryCreateOrUpdateOrderQuotationResponse actualResponse = objectMapper.readValue(responseString, FactoryCreateOrUpdateOrderQuotationResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 견적서 수정 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_수정_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        final FactoryCreateOrUpdateOrderQuotationResponse expectedResponse = FactoryCreateOrUpdateOrderQuotationResponseBuilder.updateBuild();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);
        when(factoryOrderService.updateOrderQuotation(any(), any(), any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotationWithOutFile(accessToken, orderId, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryCreateOrUpdateOrderQuotationResponse actualResponse = objectMapper.readValue(responseString, FactoryCreateOrUpdateOrderQuotationResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_견적서_작성_및_수정_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_견적서_작성_및_수정_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }


    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_작성_및_수정_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, invalidOrderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 견적 비용 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_작성_및_수정_실패_견적_비용_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.nullTotalCostBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "총 견적 비용은 필수 입력값입니다.");
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 납기일 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_작성_및_수정_실패_납기일_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.nullDeliveryDate();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "납기일은 필수 입력값입니다.");
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 거래 단계가 견적서 작성 및 수정 가능 단계(견적 대기)가 아님
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_작성_및_수정_실패_견적서작성및수정_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, order.getStage().getValue());
    }

    /**
     * 거래 견적서 작성 실패
     * - 실패 사유 : 견적서의 납기일이 거래 생성일 이전임
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_작성_실패_납기일_거래_생성일_이전() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.earlyDeliveryDateBuild();

        final Order order = OrderBuilder.build();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_QUOTATION_DELIVERY_DATE)).when(factoryOrderService).createOrderQuotation(any(), any(), any());

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        assertError(OrderErrorCode.INVALID_QUOTATION_DELIVERY_DATE, resultActions);
    }

    /**
     * 거래 견적서 작성 실패
     * - 실패 사유 : 견적서 작성 시, 요청에 견적서의 파일이 존재하지 않음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_작성_실패_견적서_파일_존재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        final Order order = OrderBuilder.build();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);
        doThrow(new CustomCommonException(OrderErrorCode.REQUIRED_QUOTATION_FILE)).when(factoryOrderService).createOrderQuotation(any(), any(), any());

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotationWithOutFile(accessToken, orderId, request);

        // then
        assertError(OrderErrorCode.REQUIRED_QUOTATION_FILE, resultActions);
    }

    /**
     * 거래 견적서 수정 실패
     * - 실패 사유 : 견적서의 납기일이 거래 생성일 이전임
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_견적서_수정_실패_납기일_거래_생성일_이전() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.earlyDeliveryDateBuild();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_QUOTATION_DELIVERY_DATE)).when(factoryOrderService).updateOrderQuotation(any(), any(), any());

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotationWithOutFile(accessToken, orderId, request);

        // then
        assertError(OrderErrorCode.INVALID_QUOTATION_DELIVERY_DATE, resultActions);
    }

    /**
     * 거래 발주서 승인 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_발주서_승인_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, orderId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_승인_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_발주서_승인_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_발주서_승인_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 거래 단계가 발주서 승인 가능 단계(견적 승인)가 아님
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_발주서_승인_실패_발주서승인_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(factoryOrderService).approvePurchaseOrder(any());

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, orderId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 거래 발주서가 존재하지 않음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_발주서_승인_실패_발주서_존재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER)).when(factoryOrderService).approvePurchaseOrder(any());

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, orderId);

        // then
        assertError(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER, resultActions);
    }

    /**
     * 거래 제작 완료 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_제작_완료_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(accessToken, orderId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_제작_완료_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_제작_완료_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_제작_완료_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 거래 단계가 견적서 제작 완료 가능 단계(제작 중)가 아님
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_제작_완료_실패_제작완료_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(factoryOrderService).changeStageToProductionCompleted(any());

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(accessToken, orderId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, orderId, baseUrl);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, orderId, baseUrl);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, orderId, baseUrl);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, invalidOrderId, baseUrl);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : base-url 파라미터 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_base_url_파라미터_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, orderId, null);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "base-url");
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : base-url 파라미터 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_base_url_파라미터_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String invalidBaseUrl = "invalid-base-url";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, orderId, invalidBaseUrl);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "base URL 형식이 유효하지 않습니다.");
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : 거래 단계가 거래 완료 가능 단계(제작 완료)가 아님
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String baseUrl = "https://base-url.com";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(factoryOrderEmailService).sendEmailForAcquirer(any(), any());

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, orderId, baseUrl);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);
        order.approvePurchaseOrder();
        order.changeStageToProductionCompleted();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_완료_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_완료_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, invalidOrderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : file 파라미터 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_file_파라미터_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompletedWithOutFile(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "file");
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : file 파라미터 empty
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_file_파라미터_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, emptyFile, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "인수자 서명 파일은 필수 입력값입니다.");
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 인수자 이름 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_이름_필드_null() throws Exception {
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.nullNameBuild();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "인수자 이름은 필수 입력값입니다.");
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 인수자 이름 필드 empty
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_이름_필드_empty() throws Exception {
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.emptyNameBuild();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "인수자 이름은 필수 입력값입니다.");
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 인수자 이름 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_이름_필드_유효성() throws Exception {
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.invalidNameBuild();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "인수자 이름의 최대 글자수는 10자입니다.");
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 연락처 필드 null
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_연락처_필드_null() throws Exception {
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.nullPhoneBuild();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처는 필수 입력값입니다.");
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 연락처 필드 유효성
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_연락처_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.invalidPhoneBuild();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "연락처 형식에 맞지 않습니다.");
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_존재하지_않는_거래() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.NOT_FOUND_ORDER)).when(orderService).getOrderById(any());

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertError(OrderErrorCode.NOT_FOUND_ORDER, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 거래 단계가 거래 완료 가능 단계(제작 완료)가 아님
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_완료_실패_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);
        order.approvePurchaseOrder();
        order.changeStageToProductionCompleted();
        order.changeStageToCompleted();

        // stub
        when(orderService.getOrderById(any())).thenReturn(order);

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, order.getStage().getValue());
    }

    /**
     * 거래의 고객 정보 조회 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_고객_정보_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryGetOrderCustomerResponse expectedResponse = FactoryGetOrderCustomerResponseBuilder.build();

        // stub
        when(factoryOrderService.getOrderCustomer(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetOrderCustomer(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryGetOrderCustomerResponse actualResponse = objectMapper.readValue(responseString, FactoryGetOrderCustomerResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 고객 정보 조회 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_고객_정보_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCustomer(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래의 고객 정보 조회 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_고객_정보_조회_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCustomer(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래의 고객 정보 조회 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_고객_정보_조회_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestGetOrderCustomer(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래의 발주서 파일 조회 성공
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_발주서_파일_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final FactoryGetPurchaseOrderFileResponse expectedResponse = FactoryGetPurchaseOrderFileResponseBuilder.build();

        // stub
        when(factoryOrderService.getOrderPurchaseOrderFile(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryGetPurchaseOrderFileResponse actualResponse = objectMapper.readValue(responseString, FactoryGetPurchaseOrderFileResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 고객 역할 (CUSTOMER)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 거래_발주서_파일_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 거래_발주서_파일_조회_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_발주서_파일_조회_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 거래 발주서가 존재하지 않음
     */
    @Test
    @WithMockUser(authorities = {"ROLE_FACTORY", "AUTHORITY_ADMIN"})
    public void 거래_발주서_파일_조회_실패_발주서_존재() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER)).when(factoryOrderService).getOrderPurchaseOrderFile(any());

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(accessToken, orderId);

        // then
        assertError(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER, resultActions);
    }

    private ResultActions requestUpdateOrderIsUrgent(String accessToken, String orderId, FactoryUpdateOrderIsUrgentRequest request) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/urgent", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderQuotation(String accessToken, String orderId, MockMultipartFile file, FactoryCreateOrUpdateOrderQuotationRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/quotation", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile quotation = new MockMultipartFile("quotation", "quotation", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(quotation)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderQuotationWithOutFile(String accessToken, String orderId, FactoryCreateOrUpdateOrderQuotationRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/quotation", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile quotation = new MockMultipartFile("quotation", "quotation", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(quotation)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestApprovePurchaseOrder(String accessToken, String orderId) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/purchase-order", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestChangeStageToProductionComplete(String accessToken, String orderId) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/stage/production-completed", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestSendEmailForAcquirer(String accessToken, String orderId, String baseUrl) throws Exception {
        return mvc.perform(post("/factory/order/{order-id}/acquirer/email-link", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .param("base-url", baseUrl))
                .andDo(print());
    }

    private ResultActions requestChangeStageToCompleted(String accessToken, String orderId, MockMultipartFile file, FactoryCreateOrderAcquirerRequest request) throws Exception{
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/stage/completed", orderId);

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile acquire = new MockMultipartFile("acquirer", "acquirer", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(acquire)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestChangeStageToCompletedWithOutFile(String accessToken, String orderId, FactoryCreateOrderAcquirerRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/stage/completed", orderId);

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile acquire = new MockMultipartFile("acquire", "acquire", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(acquire)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestGetOrderCustomer(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/factory/order/{order-id}/customer", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderPurchaseOrderFile(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/factory/order/{order-id}/purchase-order/file", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
