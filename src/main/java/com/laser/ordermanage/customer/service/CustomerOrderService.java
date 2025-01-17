package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.component.FileComponent;
import com.laser.ordermanage.common.entity.embedded.FileEntity;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.*;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponse;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.PurchaseOrder;
import com.laser.ordermanage.order.domain.type.PurchaseOrderFileType;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.CommentRepository;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.order.repository.PurchaseOrderRepository;
import com.laser.ordermanage.order.service.DrawingService;
import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

    private final FileComponent fileComponent;

    private final CommentRepository commentRepository;
    private final DrawingRepository drawingRepository;
    private final OrderRepository orderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    private final OrderService orderService;
    private final CustomerUserAccountService customerUserAccountService;
    private final CustomerDeliveryAddressService customerDeliveryAddressService;
    private final DrawingService drawingService;

    @Transactional
    public Long createOrder(String email, CustomerCreateOrderRequest request) {
        Customer customer = customerUserAccountService.getCustomerByUserEmail(email);

        Order order = request.toEntity(customer);

        Order createdOrder = orderRepository.save(order);

        List<Drawing> drawingList = request.drawingList().stream()
                .map(drawingRequest -> drawingRequest.toEntity(order))
                .collect(Collectors.toList());

        drawingRepository.saveAll(drawingList);

        return createdOrder.getId();
    }

    @Transactional
    public void updateOrderDeliveryAddress(Long orderId, CustomerUpdateOrderDeliveryAddressRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableUpdateDeliveryAddress()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        DeliveryAddress deliveryAddress = customerDeliveryAddressService.getDeliveryAddress(request.deliveryAddressId());

        order.updateDeliveryAddress(deliveryAddress);
    }

    @Transactional
    public Long createOrderDrawing(Long orderId, CustomerCreateDrawingRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableManageDrawing()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        Drawing drawing = request.toEntity(order);

        Drawing createdDrawing = drawingRepository.save(drawing);

        return createdDrawing.getId();
    }

    @Transactional
    public void updateOrderDrawing(Long orderId, Long drawingId, CustomerUpdateDrawingRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableManageDrawing()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        Drawing drawing = drawingService.getDrawingById(drawingId);

        drawing.updateProperties(request);
    }

    @Transactional
    public void deleteOrderDrawing(Long orderId, Long drawingId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableManageDrawing()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (drawingService.countDrawingByOrderId(orderId).equals(1)) {
            throw new CustomCommonException(OrderErrorCode.LAST_DRAWING_DELETE);
        }

        Drawing drawing = drawingService.getDrawingById(drawingId);

        drawingRepository.delete(drawing);
    }

    @Transactional(readOnly = true)
    public void checkAuthorityOfOrder(String email, Long orderId) {
        if (!orderService.getUserEmailByOrder(orderId).equals(email)) {
            throw new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER);
        }
    }

    @Transactional
    public void approveQuotation(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableApproveQuotation()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (!order.hasQuotation()) {
            throw new CustomCommonException(OrderErrorCode.NOT_FOUND_QUOTATION);
        }

        order.approveQuotation();
    }

    @Transactional
    public CustomerCreateOrUpdateOrderPurchaseOrderResponse createOrderPurchaseOrder(Long orderId, MultipartFile file, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (request.isValidInspectionPeriod(order)) {
            throw new CustomCommonException(OrderErrorCode.INVALID_PURCHASE_ORDER_INSPECTION_PERIOD);
        }

        if (request.isValidPaymentDate(order)) {
            throw new CustomCommonException(OrderErrorCode.INVALID_PURCHASE_ORDER_PAYMENT_DATE);
        }

        // 발주서 파일 유무 확인
        if (file == null || file.isEmpty()) {
            throw new CustomCommonException(OrderErrorCode.REQUIRED_PURCHASE_ORDER_FILE);
        }

        FileEntity<PurchaseOrderFileType> purchaseOrderFile = fileComponent.uploadFile(file, PurchaseOrderFileType::ofExtension);

        PurchaseOrder purchaseOrder = request.toEntity(purchaseOrderFile);

        PurchaseOrder createdPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        order.createPurchaseOrder(createdPurchaseOrder);

        return CustomerCreateOrUpdateOrderPurchaseOrderResponse.from(createdPurchaseOrder);
    }

    @Transactional
    public CustomerCreateOrUpdateOrderPurchaseOrderResponse updateOrderPurchaseOrder(Long orderId, MultipartFile file, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) {
        Order order = orderService.getOrderById(orderId);
        PurchaseOrder purchaseOrder = order.getPurchaseOrder();

        if (request.isValidInspectionPeriod(order)) {
            throw new CustomCommonException(OrderErrorCode.INVALID_PURCHASE_ORDER_INSPECTION_PERIOD);
        }

        if (request.isValidPaymentDate(order)) {
            throw new CustomCommonException(OrderErrorCode.INVALID_PURCHASE_ORDER_PAYMENT_DATE);
        }

        // 발주서 파일 유무 확인
        if (file != null && !file.isEmpty()) {

            FileEntity<PurchaseOrderFileType> purchaseOrderFile = fileComponent.uploadFile(file, PurchaseOrderFileType::ofExtension);

            purchaseOrder.updateFile(purchaseOrderFile);
        }

        purchaseOrder.updateProperties(request);

        return CustomerCreateOrUpdateOrderPurchaseOrderResponse.from(purchaseOrder);
    }

    @Transactional
    public void deleteOrderByStageNotCompleted(String email) {
        // 거래 목록 조회 (견적 대기, 견적 승인, 제작 중, 제작 완료)
        List<Long> orderIdList = orderRepository.findIdByCustomerAndStageNotCompleted(email);

        // 거래 도면 데이터 삭제
        drawingRepository.deleteAllByOrderList(orderIdList);

        // 거래 댓글 데이터 삭제
        commentRepository.deleteAllByOrderList(orderIdList);

        // 거래 데이터 삭제 및 연관 데이터 삭제 (거래 제조 서비스, 거래 후처리 서비스, 거래 배송지, 견적서, 발주서)
        orderRepository.deleteAllByIdIn(orderIdList);
    }

    @Transactional
    public void deleteOrderByStageCompleted(String email) {
        // 거래 목록 조회 (거래 완료)
        List<Order> orderList = orderRepository.findByCustomerAndStageCompleted(email);
        List<Long> orderIdList = orderList.stream().map(order -> order.getId()).toList();

        // 거래 댓글과 사용자의 연관관계 제거
        commentRepository.updateCommentUserAsNullByUserAndOrder(email, orderIdList);

        // 거래와 고객의 연관관계 제거 및 삭제 표시
        orderList.forEach(order -> order.delete());
    }
}
