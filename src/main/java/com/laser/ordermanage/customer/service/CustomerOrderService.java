package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.CreateCustomerOrderRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateOrderDeliveryAddressRequest;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.order.service.OrderService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

    private final EntityManager entityManager;

    private final DrawingRepository drawingRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    private final OrderService orderService;
    private final CustomerDeliveryAddressService customerDeliveryAddressService;
    private final MailService mailService;

    @Transactional
    public void createOrder(User user, CreateCustomerOrderRequest request) {
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
        String toEmail = "admin@kumoh.org";

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

        mailService.sendEmail(toEmail, title, content);
    }
}
