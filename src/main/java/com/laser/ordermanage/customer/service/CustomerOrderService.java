package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.CustomerCreateDrawingRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrderRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateDrawingRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateOrderDeliveryAddressRequest;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.order.service.DrawingService;
import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

    private final DrawingRepository drawingRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    private final OrderService orderService;
    private final CustomerDeliveryAddressService customerDeliveryAddressService;
    private final DrawingService drawingService;
    private final MailService mailService;

    @Transactional
    public void createOrder(User user, CustomerCreateOrderRequest request) {
        Customer customer = customerRepository.findFirstByUserEmail(user.getUsername());
        DeliveryAddress deliveryAddress = customerDeliveryAddressService.getDeliveryAddress(request.getDeliveryAddressId());

        OrderManufacturing orderManufacturing = OrderManufacturing.ofRequest(request.getManufacturing());
        OrderPostProcessing orderPostProcessing = OrderPostProcessing.ofRequest(request.getPostProcessing());

        Order order = Order.builder()
                .customer(customer)
                .deliveryAddress(deliveryAddress)
                .name(request.getName())
                .imgUrl(request.getOrderImgUrl())
                .manufacturing(orderManufacturing)
                .postProcessing(orderPostProcessing)
                .request(request.getRequest())
                .isNewIssue(request.getIsNewIssue())
                .build();

        Order createdOrder = orderRepository.save(order);

        List<Drawing> drawingList = new ArrayList<>();
        request.getDrawingList().forEach(
                drawingRequest -> {
                    drawingList.add(
                            Drawing.builder()
                                    .order(createdOrder)
                                    .fileName(drawingRequest.getFileName())
                                    .fileSize(drawingRequest.getFileSize())
                                    .fileType(drawingRequest.getFileType())
                                    .fileUrl(drawingRequest.getFileUrl())
                                    .thumbnailUrl(drawingRequest.getThumbnailUrl())
                                    .count(drawingRequest.getCount())
                                    .ingredient(drawingRequest.getIngredient())
                                    .thickness(drawingRequest.getThickness())
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

        DeliveryAddress deliveryAddress = customerDeliveryAddressService.getDeliveryAddress(request.getDeliveryAddressId());

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
                .fileName(request.getFileName())
                .fileSize(request.getFileSize())
                .fileType(request.getFileType())
                .fileUrl(request.getFileUrl())
                .thumbnailUrl(request.getThumbnailUrl())
                .count(request.getCount())
                .ingredient(request.getIngredient())
                .thickness(request.getThickness())
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
        if (orderService.getUserEmailByOrder(orderId).equals(user.getUsername())) {
            return;
        }

        throw new CustomCommonException(ErrorCode.DENIED_ACCESS_TO_ENTITY, "order");
    }
}
