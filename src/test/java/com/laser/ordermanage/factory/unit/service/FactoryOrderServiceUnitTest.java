package com.laser.ordermanage.factory.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.factory.dto.request.*;
import com.laser.ordermanage.factory.dto.response.*;
import com.laser.ordermanage.factory.service.FactoryOrderService;
import com.laser.ordermanage.order.domain.*;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.AcquirerRepository;
import com.laser.ordermanage.order.repository.QuotationRepository;
import com.laser.ordermanage.order.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class FactoryOrderServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private FactoryOrderService factoryOrderService;

    @Mock
    private QuotationRepository quotationRepository;

    @Mock
    private AcquirerRepository acquirerRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private S3Service s3Service;

    /**
     * 거래 긴급 설정 성공
     */
    @Test
    public void updateOrderIsUrgent_성공() {
        // given
        final Order order = OrderBuilder.build();

        final Long orderId = 1L;
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.isUrgentTrueBuild();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        factoryOrderService.updateOrderIsUrgent(orderId, request);

        // then
        Assertions.assertThat(order.getIsUrgent()).isTrue();
    }

    /**
     * 거래 긴급 설정 실패
     * - 실패 사유 : 거래 단계가 거래 긴급 설정 가능 단계(견적 대기, 견적 승인, 제작 중, 제작 완료)가 아님
     */
    @Test
    public void updateOrderIsUrgent_실패_INVALID_ORDER_STAGE() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        final Long orderId = 1L;
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.isUrgentTrueBuild();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> factoryOrderService.updateOrderIsUrgent(orderId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(String.format(OrderErrorCode.INVALID_ORDER_STAGE.getMessage(), order.getStage().getValue()));
    }

    /**
     * 거래 견적서 작성 성공
     */
    @Test
    public void createOrderQuotation_성공() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        ReflectionTestUtils.setField(order, "createdAt", LocalDateTime.of(2023, 10, 2, 10, 20, 30));
        final Quotation quotation = QuotationBuilder.build();

        final Long orderId = 1L;
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();
        final FactoryCreateOrUpdateOrderQuotationResponse expectedResponse = FactoryCreateOrUpdateOrderQuotationResponseBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(s3Service.upload(any(), (MultipartFile) any(), eq("quotation.xlsx"))).thenReturn("quotation.xlsx.png");
        when(quotationRepository.save(any())).thenReturn(quotation);

        // when
        final FactoryCreateOrUpdateOrderQuotationResponse actualResponse = factoryOrderService.createOrderQuotation(orderId, file, request);

        // then
        Assertions.assertThat(order.getQuotation()).isNotNull();
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 견적서 작성 실패
     * - 실패 사유 : 견적서의 납기일이 거래 생성일 이전임
     */
    @Test
    public void createOrderQuotation_실패_INVALID_QUOTATION_DELIVERY_DATE() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        ReflectionTestUtils.setField(order, "createdAt", LocalDateTime.of(2023, 10, 13, 10, 20, 30));

        final Long orderId = 1L;
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.earlyDeliveryDateBuild();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> factoryOrderService.createOrderQuotation(orderId, file, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.INVALID_QUOTATION_DELIVERY_DATE.getMessage());
    }

    /**
     * 거래 견적서 작성 실패
     * - 실패 사유 : 견적서의 파일이 존재하지 않음
     */
    @Test
    public void createOrderQuotation_실패_REQUIRED_QUOTATION_FILE() {
        // given
        final Order order = OrderBuilder.build();
        ReflectionTestUtils.setField(order, "createdAt", LocalDateTime.of(2023, 10, 2, 10, 20, 30));

        final Long orderId = 1L;
        final MockMultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> factoryOrderService.createOrderQuotation(orderId, emptyFile, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.REQUIRED_QUOTATION_FILE.getMessage());
    }

    /**
     * 거래 견적서 수정 성공
     */
    @Test
    public void updateOrderQuotation_성공() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        ReflectionTestUtils.setField(order, "createdAt", LocalDateTime.of(2023, 10, 2, 10, 20, 30));
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        final Long orderId = 1L;
        final MockMultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();
        final FactoryCreateOrUpdateOrderQuotationResponse expectedResponse = FactoryCreateOrUpdateOrderQuotationResponseBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        final FactoryCreateOrUpdateOrderQuotationResponse actualResponse = factoryOrderService.updateOrderQuotation(orderId, emptyFile, request);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        Assertions.assertThat(quotation.getTotalCost()).isEqualTo(request.totalCost());
        Assertions.assertThat(quotation.getDeliveryDate()).isEqualTo(request.deliveryDate());
    }

    /**
     * 거래 견적서 수정 실패
     * - 실패 사유 : 견적서의 납기일이 거래 생성일 이전임
     */
    @Test
    public void updateOrderQuotation_실패_INVALID_QUOTATION_DELIVERY_DATE() {
        // given
        final Order order = OrderBuilder.build();
        ReflectionTestUtils.setField(order, "createdAt", LocalDateTime.of(2023, 10, 13, 10, 20, 30));
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        final Long orderId = 1L;
        final MockMultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.earlyDeliveryDateBuild();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> factoryOrderService.updateOrderQuotation(orderId, emptyFile, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.INVALID_QUOTATION_DELIVERY_DATE.getMessage());
    }

    /**
     * 거래 발주서 승인 성공
     */
    @Test
    public void approvePurchaseOrder_성공() {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        factoryOrderService.approvePurchaseOrder(orderId);

        // then
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.IN_PRODUCTION);
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 거래 단계가 발주서 승인 가능 단계(견적 승인)가 아님
     */
    @Test
    public void approvePurchaseOrder_실패_INVALIE_ORDER_STAGE() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> factoryOrderService.approvePurchaseOrder(orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(String.format(OrderErrorCode.INVALID_ORDER_STAGE.getMessage(), order.getStage().getValue()));
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 거래 발주서가 존재하지 않음
     */
    @Test
    public void approvePurchaseOrder_실패_NOT_FOUND_PURCHASE_ORDER() {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> factoryOrderService.approvePurchaseOrder(orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER.getMessage());
    }

    /**
     * 거래 제작 완료 성공
     */
    @Test
    public void changeStageToProductionCompleted_성공() {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        order.approveQuotation();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);
        order.approvePurchaseOrder();

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        factoryOrderService.changeStageToProductionCompleted(orderId);

        // then
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.PRODUCTION_COMPLETED);
    }

    /**
     * 거래 제작 완료 실패
     * 실패 사유 : 거래 단계가 견적서 제작 완료 가능 단계(제작 중)가 아님
     */
    @Test
    public void changeStageToProductionCompleted_실패_INVALIE_ORDER_STAGE() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> factoryOrderService.changeStageToProductionCompleted(orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(String.format(OrderErrorCode.INVALID_ORDER_STAGE.getMessage(), order.getStage().getValue()));
    }

    /**
     * 거래 인수자 생성 성공
     */
    @Test
    public void createOrderAcquirer_성공() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        final Acquirer acquirer = AcquirerBuilder.build();

        final Long orderId = 1L;
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(s3Service.upload(any(), (MultipartFile) any(), eq("acquirer-signature.png"))).thenReturn("acquirer-signature-url.png");
        when(acquirerRepository.save(any())).thenReturn(acquirer);

        // when
        factoryOrderService.createOrderAcquirer(orderId, request, file);

        // then
        Assertions.assertThat(order.getAcquirer()).isNotNull();
        Assertions.assertThat(order.getAcquirer().getName()).isEqualTo(request.name());
        Assertions.assertThat(order.getAcquirer().getPhone()).isEqualTo(request.phone());
    }

    /**
     * 거래 단계 완료 변경 성공
     */
    @Test
    public void changeStageToCompleted_성공() {
        // given
        final Order order = OrderBuilder.build();

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        factoryOrderService.changeStageToCompleted(orderId);

        // then
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.COMPLETED);
        Assertions.assertThat(order.getCompletedAt()).isNotNull();
        Assertions.assertThat(order.getCustomer().getIsNew()).isFalse();
    }

    /**
     * 거래 고객 조회 성공
     */
    @Test
    public void getOrderCustomer_성공() {
        // given
        final Order order = OrderBuilder.build();

        final Long orderId = 1L;
        final FactoryGetOrderCustomerResponse expectedResponse = FactoryGetOrderCustomerResponseBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        final FactoryGetOrderCustomerResponse actualResponse = factoryOrderService.getOrderCustomer(orderId);

        // then
        Assertions.assertThat(actualResponse.orderName()).isEqualTo(expectedResponse.orderName());
        Assertions.assertThat(actualResponse.customer().name()).isEqualTo(expectedResponse.customer().name());
        Assertions.assertThat(actualResponse.customer().company()).isEqualTo(expectedResponse.customer().company());
        Assertions.assertThat(actualResponse.customer().phone()).isEqualTo(expectedResponse.customer().phone());
        Assertions.assertThat(actualResponse.customer().email()).isEqualTo(expectedResponse.customer().email());
    }

    /**
     * 거래 발주서 파일 조회 성공
     */
    @Test
    public void getOrderPurchaseOrderFile_성공() {
        // given
        final Order order = OrderBuilder.build();
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);

        final Long orderId = 1L;
        final FactoryGetPurchaseOrderFileResponse expectedResponse = FactoryGetPurchaseOrderFileResponseBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        final FactoryGetPurchaseOrderFileResponse actualResponse = factoryOrderService.getOrderPurchaseOrderFile(orderId);

        // then
        Assertions.assertThat(actualResponse.fileName()).isEqualTo(expectedResponse.fileName());
        Assertions.assertThat(actualResponse.fileUrl()).isEqualTo(expectedResponse.fileUrl());
    }

    /**
     * 거래 발주서 파일 조회 실패
     * - 실패 사유 : 거래 발주서가 존재하지 않음
     */
    @Test
    public void getOrderPurchaseOrderFile_실패_NOT_FOUND_PURCHASE_ORDER() {
        // given
        final Order order = OrderBuilder.build();

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> factoryOrderService.getOrderPurchaseOrderFile(orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER.getMessage());
    }
}
