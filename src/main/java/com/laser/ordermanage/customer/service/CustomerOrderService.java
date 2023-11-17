package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.CreateCustomerOrderRequest;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import com.laser.ordermanage.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public void createOrder(User user, CreateCustomerOrderRequest createCustomerOrderRequest) {
        Customer customer = customerRepository.findFirstByUserEmail(user.getUsername());
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findFirstById(createCustomerOrderRequest.getDeliveryAddressId());
        OrderManufacturing orderManufacturing = OrderManufacturing.ofRequest(createCustomerOrderRequest.getManufacturing());
        OrderPostProcessing orderPostProcessing = OrderPostProcessing.ofRequest(createCustomerOrderRequest.getPostProcessing());

        String orderImgUrl = createCustomerOrderRequest.getDrawingList().get(0).getThumbnailImgUrl();

        Order order = Order.builder()
                .customer(customer)
                .deliveryAddress(deliveryAddress)
                .name(createCustomerOrderRequest.getName())
                .imgUrl(orderImgUrl)
                .manufacturing(orderManufacturing)
                .postProcessing(orderPostProcessing)
                .request(createCustomerOrderRequest.getRequest())
                .isNewIssue(createCustomerOrderRequest.getIsNewIssue())
                .build();

        orderRepository.save(order);
    }

}