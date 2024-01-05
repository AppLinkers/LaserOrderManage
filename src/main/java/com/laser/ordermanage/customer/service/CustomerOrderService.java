package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.*;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponse;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.order.domain.*;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.order.repository.PurchaseOrderRepository;
import com.laser.ordermanage.order.service.DrawingService;
import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

    private final DrawingRepository drawingRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    private final OrderService orderService;
    private final CustomerDeliveryAddressService customerDeliveryAddressService;
    private final DrawingService drawingService;
    private final MailService mailService;
    private final S3Service s3Service;

    @Transactional
    public void createOrder(User user, CustomerCreateOrderRequest request) {
        Customer customer = customerRepository.findFirstByUserEmail(user.getUsername());

        OrderDeliveryAddress deliveryAddress = OrderDeliveryAddress.ofRequest(request.deliveryAddress());

        OrderManufacturing orderManufacturing = OrderManufacturing.ofRequest(request.manufacturing());
        OrderPostProcessing orderPostProcessing = OrderPostProcessing.ofRequest(request.postProcessing());

        Order order = Order.builder()
                .customer(customer)
                .deliveryAddress(deliveryAddress)
                .name(request.name())
                .imgUrl(request.getOrderImgUrl())
                .manufacturing(orderManufacturing)
                .postProcessing(orderPostProcessing)
                .request(request.request())
                .isNewIssue(request.isNewIssue())
                .build();

        Order createdOrder = orderRepository.save(order);

        List<Drawing> drawingList = new ArrayList<>();
        request.drawingList().forEach(
                drawingRequest -> {
                    drawingList.add(
                            Drawing.builder()
                                    .order(createdOrder)
                                    .fileName(drawingRequest.fileName())
                                    .fileSize(drawingRequest.fileSize())
                                    .fileType(drawingRequest.fileType())
                                    .fileUrl(drawingRequest.fileUrl())
                                    .thumbnailUrl(drawingRequest.thumbnailUrl())
                                    .count(drawingRequest.count())
                                    .ingredient(drawingRequest.ingredient())
                                    .thickness(drawingRequest.thickness())
                                    .build()
                    );
                }
        );

        drawingRepository.saveAll(drawingList);
    }

    @Transactional
    public Order updateOrderDeliveryAddress(Long orderId, CustomerUpdateOrderDeliveryAddressRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableUpdateDeliveryAddress()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        DeliveryAddress deliveryAddress = customerDeliveryAddressService.getDeliveryAddress(request.deliveryAddressId());

        order.updateDeliveryAddress(deliveryAddress);

        return order;
    }

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderDeliveryAddress(Order order) {
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 배송지 수정] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 배송지가 수정되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(" 고객님의 ")
                .append(order.getName())
                .append(" 거래 배송지가 수정되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmailToFactory(title, content);
    }

    @Transactional
    public Drawing createOrderDrawing(Long orderId, CustomerCreateDrawingRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableManageDrawing()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        Drawing drawing = Drawing.builder()
                .order(order)
                .fileName(request.fileName())
                .fileSize(request.fileSize())
                .fileType(request.fileType())
                .fileUrl(request.fileUrl())
                .thumbnailUrl(request.thumbnailUrl())
                .count(request.count())
                .ingredient(request.ingredient())
                .thickness(request.thickness())
                .build();

        Drawing savedDrawing = drawingRepository.save(drawing);

        return savedDrawing;
    }

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderDrawing(Order order) {
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 도면 추가] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 도면이 추가되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(" 고객님의 ")
                .append(order.getName())
                .append(" 거래 도면이 추가되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmailToFactory(title, content);
    }

    @Transactional
    public Order updateOrderDrawing(Long orderId, Long drawingId, CustomerUpdateDrawingRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableManageDrawing()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        Drawing drawing = drawingService.getDrawingByOrderAndId(order, drawingId);

        drawing.updateDrawingProperties(request);

        return order;
    }

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderDrawing(Order order) {
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 도면 항목 수정] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 도면 항목이 수정되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(" 고객님의 ")
                .append(order.getName())
                .append(" 거래 도면 항목이 수정되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmailToFactory(title, content);
    }

    @Transactional
    public Order deleteOrderDrawing(Long orderId, Long drawingId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableManageDrawing()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (drawingService.countDrawingByOrder(order).equals(1)) {
            throw new CustomCommonException(ErrorCode.LAST_DRAWING_DELETE);
        }

        Drawing drawing = drawingService.getDrawingByOrderAndId(order, drawingId);

        drawingRepository.delete(drawing);

        return order;
    }

    @Transactional(readOnly = true)
    public void sendEmailForDeleteOrderDrawing(Order order) {
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 도면 삭제] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 도면이 삭제되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(" 고객님의 ")
                .append(order.getName())
                .append(" 거래 도면이 삭제되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmailToFactory(title, content);
    }

    @Transactional(readOnly = true)
    public void checkAuthorityOfOrder(User user, Long orderId) {
        if (!orderService.getUserEmailByOrder(orderId).equals(user.getUsername())) {
            throw new CustomCommonException(ErrorCode.DENIED_ACCESS_TO_ENTITY, "order");
        }
    }

    @Transactional
    public Order approveQuotation(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableApproveQuotation()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (!order.hasQuotation()) {
            throw new CustomCommonException(ErrorCode.MISSING_QUOTATION);
        }

        order.approveQuotation();

        return order;
    }

    @Transactional(readOnly = true)
    public void sendEmailForApproveQuotation(Order order) {
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 견적서 승인] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 견적서가 승인되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(" 고객님의 ")
                .append(order.getName())
                .append(" 거래 견적서가 승인되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmailToFactory(title, content);
    }

    @Transactional
    public CustomerCreateOrUpdateOrderPurchaseOrderResponse createOrderPurchaseOrder(Order order, MultipartFile file, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) {
        // 발주서 파일 유무 확인
        if (file == null || file.isEmpty()) {
            throw new CustomCommonException(ErrorCode.MISSING_PURCHASE_ORDER_FILE);
        }

        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();

        // 발주서 파일 업로드
        String purchaseOrderFileUrl = uploadPurchaseOrderFile(file);

        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .inspectionPeriod(request.inspectionPeriod())
                .inspectionCondition(request.inspectionCondition())
                .paymentDate(request.paymentDate())
                .fileName(fileName)
                .fileSize(fileSize)
                .fileUrl(purchaseOrderFileUrl)
                .build();

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        order.createPurchaseOrder(savedPurchaseOrder);

        return CustomerCreateOrUpdateOrderPurchaseOrderResponse.from(savedPurchaseOrder);
    }

    @Transactional(readOnly = true)
    public void sendMailForCreateOrderPurchaseOrder(Order order) {
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 발주서 작성] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 발주서가 작성되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(" 고객님의 ")
                .append(order.getName())
                .append(" 거래 발주서가 작성되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmailToFactory(title, content);
    }

    @Transactional
    public CustomerCreateOrUpdateOrderPurchaseOrderResponse updateOrderPurchaseOrder(Order order, MultipartFile file, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) {
        PurchaseOrder purchaseOrder = order.getPurchaseOrder();

        // 발주서 파일 유무 확인
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Long fileSize = file.getSize();

            // 발주서 파일 업로드
            String purchaseOrderFileUrl = uploadPurchaseOrderFile(file);

            purchaseOrder.updateFile(fileName, fileSize, purchaseOrderFileUrl);
        }

        purchaseOrder.updateProperties(request);

        return CustomerCreateOrUpdateOrderPurchaseOrderResponse.from(purchaseOrder);
    }

    @Transactional(readOnly = true)
    public void sendMailForUpdateOrderPurchaseOrder(Order order) {
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 발주서 수정] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 발주서가 수정되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(" 고객님의 ")
                .append(order.getName())
                .append(" 거래 발주서가 수정되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmailToFactory(title, content);
    }

    private String uploadPurchaseOrderFile(MultipartFile file) {
        return s3Service.upload("purchase-order", file);
    }
}
