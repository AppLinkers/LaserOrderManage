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
    public void createOrder(User user, CreateCustomerOrderRequest request) {
        Customer customer = customerRepository.findFirstByUserEmail(user.getUsername());
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findFirstById(request.getDeliveryAddressId());

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

        orderRepository.save(order);
    }

}
