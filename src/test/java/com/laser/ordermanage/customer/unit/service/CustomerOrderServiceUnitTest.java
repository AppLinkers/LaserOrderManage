package com.laser.ordermanage.customer.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.component.FileComponent;
import com.laser.ordermanage.common.entity.FileBuilder;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.domain.DeliveryAddressBuilder;
import com.laser.ordermanage.customer.dto.request.*;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponse;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import com.laser.ordermanage.customer.service.CustomerUserAccountService;
import com.laser.ordermanage.order.domain.*;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.CommentRepository;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.order.repository.PurchaseOrderRepository;
import com.laser.ordermanage.order.service.DrawingService;
import com.laser.ordermanage.order.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerOrderServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private CustomerOrderService customerOrderService;

    @Mock
    private FileComponent fileComponent;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private DrawingRepository drawingRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerUserAccountService customerUserAccountService;

    @Mock
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    @Mock
    private DrawingService drawingService;

    /**
     * 거래 생성 성공
     */
    @Test
    public void createOrder_성공() {
        // given
        final Order expectedOrder = OrderBuilder.build();

        final String userEmail = "user@gmail.com";
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // stub
        when(customerUserAccountService.getCustomerByUserEmail(userEmail)).thenReturn(expectedOrder.getCustomer());
        when(orderRepository.save(any())).thenReturn(expectedOrder);

        // when
        final Long actualOrderId = customerOrderService.createOrder(userEmail, request);

        // then
        Assertions.assertThat(actualOrderId).isEqualTo(expectedOrder.getId());
        verify(drawingRepository, times(1)).saveAll(any());
    }

    /**
     * 거래 베송지 수정 성공
     */
    @Test
    public void updateOrderDeliveryAddress_성공() {
        // given
        final Order order = OrderBuilder.build();
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build2();

        final Long orderId = 1L;
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(customerDeliveryAddressService.getDeliveryAddress(request.deliveryAddressId())).thenReturn(deliveryAddress);

        // when
        customerOrderService.updateOrderDeliveryAddress(orderId, request);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 거래 단계가 배송지 수정 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    public void updateOrderDeliveryAddress_실패_INVALID_ORDER_STAGE() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        final Long orderId = 1L;
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.updateOrderDeliveryAddress(orderId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(String.format(OrderErrorCode.INVALID_ORDER_STAGE.getMessage(), order.getStage().getValue()));
    }

    /**
     * 거래 도면 항목 추가 성공
     */
    @Test
    public void createOrderDrawing_성공() {
        // given
        final Order order = OrderBuilder.build();
        final Drawing expectedDrawing = DrawingBuilder.build();

        final Long orderId = 1L;
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(drawingRepository.save(any())).thenReturn(expectedDrawing);

        // when
        final Long actualDrawingId = customerOrderService.createOrderDrawing(orderId, request);

        // then
        Assertions.assertThat(actualDrawingId).isEqualTo(expectedDrawing.getId());
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 거래 단계가 도면 항목 추가 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    public void createOrderDrawing_실패_INVALID_ORDER_STAGE() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        final Long orderId = 1L;
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.createOrderDrawing(orderId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(String.format(OrderErrorCode.INVALID_ORDER_STAGE.getMessage(), order.getStage().getValue()));
    }

    /**
     * 거래 도면 항목 수정 성공
     */
    @Test
    public void updateOrderDrawing_성공() {
        // given
        final Order order = OrderBuilder.build();
        final Drawing drawing = DrawingBuilder.build();

        final Long orderId = 1L;
        final Long drawingId = 1L;
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(drawingService.getDrawingById(drawingId)).thenReturn(drawing);

        // when
        customerOrderService.updateOrderDrawing(orderId, drawingId, request);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 거래 단계가 도면 항목 수정 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    public void updateOrderDrawing_실패_INVALID_ORDER_STAGE() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        final Long orderId = 1L;
        final Long drawingId = 1L;
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.updateOrderDrawing(orderId, drawingId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(String.format(OrderErrorCode.INVALID_ORDER_STAGE.getMessage(), order.getStage().getValue()));
    }

    /**
     * 거래 도면 항목 삭제 성공
     */
    @Test
    public void deleteOrderDrawing_성공() {
        // given
        final Order order = OrderBuilder.build();
        final Drawing drawing = DrawingBuilder.build();

        final Long orderId = 1L;
        final Long drawingId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(drawingService.countDrawingByOrderId(orderId)).thenReturn(2);
        when(drawingService.getDrawingById(drawingId)).thenReturn(drawing);

        // when
        customerOrderService.deleteOrderDrawing(orderId, drawingId);

        // then
        verify(drawingRepository, times(1)).delete(any());
    }

    /**
     * 거래 도면 항목 삭제 실패
     * - 실패 사유 : 거래 단계가 도면 삭제 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    public void deleteOrderDrawing_실패_INVALID_ORDER_STAGE() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        final Long orderId = 1L;
        final Long drawingId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.deleteOrderDrawing(orderId, drawingId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(String.format(OrderErrorCode.INVALID_ORDER_STAGE.getMessage(), order.getStage().getValue()));
    }

    /**
     * 거래 도면 항목 삭제 실패
     * - 실패 사유 : 거래 마지막 도면을 삭제할 수 없음
     */
    @Test
    public void deleteOrderDrawing_실패_LAST_DRAWING_DELETE() {
        // given
        final Order order = OrderBuilder.build();

        final Long orderId = 1L;
        final Long drawingId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(drawingService.countDrawingByOrderId(orderId)).thenReturn(1);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.deleteOrderDrawing(orderId, drawingId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.LAST_DRAWING_DELETE.getMessage());
    }

    /**
     * 거래에 대한 고객 사용자의 권한 확인 성공
     */
    @Test
    public void checkAuthorityOfOrder_성공() {
        // given
        final String userEmail = "user@gmail.com";

        final Long orderId = 1L;

        // stub
        when(orderService.getUserEmailByOrder(orderId)).thenReturn(userEmail);

        // when
        customerOrderService.checkAuthorityOfOrder(userEmail, orderId);
    }

    /**
     * 거래에 대한 고객 사용자의 권한 확인 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void checkAuthorityOfOrder_실패_DENIED_ACCESS_TO_ORDER() {
        // given
        final String userEmail = "user@gmail.com";
        final String userEmailOfOrder = "user-order@gmail.com";

        final Long orderId = 1L;

        // stub
        when(orderService.getUserEmailByOrder(orderId)).thenReturn(userEmailOfOrder);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.checkAuthorityOfOrder(userEmail, orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.DENIED_ACCESS_TO_ORDER.getMessage());
    }

    /**
     * 거래의 견적서 승인 성공
     */
    @Test
    public void approveQuotation_성공() {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        customerOrderService.approveQuotation(orderId);

        // then
        Assertions.assertThat(order.getStage()).isEqualTo(Stage.QUOTE_APPROVAL);
    }

    /**
     * 거래의 견적서 승인 실패
     * - 실패 사유 : 거래 단계가 견적서 승인 가능 단계(견적 대기)가 아님
     */
    @Test
    public void approveQuotation_실패_INVALIE_ORDER_STAGE() {
        // given
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.approveQuotation(orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(String.format(OrderErrorCode.INVALID_ORDER_STAGE.getMessage(), order.getStage().getValue()));
    }

    /**
     * 거래의 견적서 승인 실패
     * - 실패 사유 : 거래 견적서가 존재하지 않음
     */
    @Test
    public void approveQuotation_실패_NOT_FOUND_QUOTATION() {
        // given
        final Order order = OrderBuilder.build();

        final Long orderId = 1L;

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.approveQuotation(orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_QUOTATION.getMessage());
    }

    /**
     * 거래의 발주서 작성 성공
     */
    @Test
    public void createOrderPurchaseOrder_성공() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();

        final Long orderId = 1L;
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.build();
        final CustomerCreateOrUpdateOrderPurchaseOrderResponse expectedResponse = CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(fileComponent.uploadFile(any(), any())).thenReturn(FileBuilder.purchaseOrderFileBuild());
        when(purchaseOrderRepository.save(any())).thenReturn(purchaseOrder);

        // when
        final CustomerCreateOrUpdateOrderPurchaseOrderResponse actualResponse = customerOrderService.createOrderPurchaseOrder(orderId, file, request);

        // then
        Assertions.assertThat(order.getPurchaseOrder()).isNotNull();
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 발주서 작성 실패
     * - 실패 사유 : 발주서의 검수기간이 거래 납기일 이전임
     */
    @Test
    public void createOrderPurchaseOrder_실패_INVALID_PURCHASE_ORDER_INSPECTION_PERIOD() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        final Long orderId = 1L;
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.earlyInspectionPeriodBuild();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.createOrderPurchaseOrder(orderId, file, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.INVALID_PURCHASE_ORDER_INSPECTION_PERIOD.getMessage());
    }

    /**
     * 거래 발주서 작성 실패
     * - 실패 사유 : 발주서의 지급일이 거래 납기일 이전임
     */
    @Test
    public void createOrderPurchaseOrder_실패_INVALID_PURCHASE_ORDER_PAYMENT_DATE() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        final Long orderId = 1L;
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.earlyPaymentDateBuild();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.createOrderPurchaseOrder(orderId, file, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.INVALID_PURCHASE_ORDER_PAYMENT_DATE.getMessage());
    }

    /**
     * 거래 발주서 작성 실패
     * - 실패 사유 : 발주서의 파일이 존재하지 않음
     */
    @Test
    public void createOrderPurchaseOrder_실패_REQUIRED_PURCHASE_ORDER_FILE() {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);

        final Long orderId = 1L;
        final MockMultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.createOrderPurchaseOrder(orderId, emptyFile, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.REQUIRED_PURCHASE_ORDER_FILE.getMessage());
    }

    /**
     * 거래 발주서 수정 성공
     */
    @Test
    public void updateOrderPurchaseOrder_성공() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);

        final Long orderId = 1L;
        final MockMultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.build();
        final CustomerCreateOrUpdateOrderPurchaseOrderResponse expectedResponse = CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder.build();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when
        final CustomerCreateOrUpdateOrderPurchaseOrderResponse actualResponse = customerOrderService.updateOrderPurchaseOrder(orderId, emptyFile, request);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        Assertions.assertThat(purchaseOrder.getInspectionPeriod()).isEqualTo(request.inspectionPeriod());
        Assertions.assertThat(purchaseOrder.getInspectionCondition()).isEqualTo(request.inspectionCondition());
        Assertions.assertThat(purchaseOrder.getPaymentDate()).isEqualTo(request.paymentDate());
    }

    /**
     * 거래 발주서 수정 실패
     * - 실패 사유 : 발주서의 검수기간이 거래 납기일 이전임
     */
    @Test
    public void updateOrderPurchaseOrder_실패_INVALID_PURCHASE_ORDER_INSPECTION_PERIOD() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);

        final Long orderId = 1L;
        final MockMultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.earlyInspectionPeriodBuild();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.updateOrderPurchaseOrder(orderId, emptyFile, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.INVALID_PURCHASE_ORDER_INSPECTION_PERIOD.getMessage());
    }

    /**
     * 거래 발주서 수정 실패
     * - 실패 사유 : 발주서의 지급일이 거래 납기일 이전임
     */
    @Test
    public void updateOrderPurchaseOrder_실패_INVALID_PURCHASE_ORDER_PAYMENT_DATE() throws Exception {
        // given
        final Order order = OrderBuilder.build();
        final Quotation quotation = QuotationBuilder.build();
        order.createQuotation(quotation);
        final PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();
        order.createPurchaseOrder(purchaseOrder);

        final Long orderId = 1L;
        final MockMultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.earlyPaymentDateBuild();

        // stub
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // when & then
        Assertions.assertThatThrownBy(() -> customerOrderService.updateOrderPurchaseOrder(orderId, emptyFile, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.INVALID_PURCHASE_ORDER_PAYMENT_DATE.getMessage());
    }

    /**
     * 고객의 거래 중, (견적 대기, 견적 승인, 제작 중, 제작 완료) 단계의 거래 전체 삭제 성공
     */
    @Test
    public void deleteOrderByStageNotCompleted_성공() {
        // given
        final List<Long> orderIdList = List.of(1L);

        final String email = "user@gmail.com";

        // stub
        when(orderRepository.findIdByCustomerAndStageNotCompleted(email)).thenReturn(orderIdList);

        // when
        customerOrderService.deleteOrderByStageNotCompleted(email);

        // then
        verify(drawingRepository, times(1)).deleteAllByOrderList(orderIdList);
        verify(commentRepository, times(1)).deleteAllByOrderList(orderIdList);
        verify(orderRepository, times(1)).deleteAllByIdIn(orderIdList);
    }

    /**
     * 고객의 거래 중, (거래 완료) 단계의 거래 삭제 표시 성공
     */
    @Test
    public void deleteOrderByStageCompleted_성공() {
        // given
        final Order order = OrderBuilder.build();
        final List<Order> orderList = List.of(order);

        final String email = "user@gmail.com";

        // stub
        when(orderRepository.findByCustomerAndStageCompleted(email)).thenReturn(orderList);

        // when
        customerOrderService.deleteOrderByStageCompleted(email);

        // then
        verify(commentRepository, times(1)).updateCommentUserAsNullByUserAndOrder(eq(email), anyList());
        Assertions.assertThat(order.getCustomer()).isNull();
        Assertions.assertThat(order.getIsDeleted()).isTrue();
    }
}
