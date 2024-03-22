package com.laser.ordermanage.order.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderBuilder;
import com.laser.ordermanage.order.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = OrderRepository.class)
public class OrderRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    protected OrderRepository orderRepository;


    @Test
    public void findFirstById_존재_O() {
        // given
        final Long expectedOrderId = 1L;
        final Order expectedOrder = OrderBuilder.build();

        // when
        final Optional<Order> optionalOrder = orderRepository.findFirstById(expectedOrderId);

        // then
        Assertions.assertThat(optionalOrder.isPresent()).isTrue();
        optionalOrder.ifPresent(
                actualOrder -> {
                    Assertions.assertThat(actualOrder.getId()).isEqualTo(expectedOrderId);
                    Assertions.assertThat(actualOrder.getCustomer().getUser().getEmail()).isEqualTo(expectedOrder.getCustomer().getUser().getEmail());
                    Assertions.assertThat(actualOrder.getCustomer().getUser().getRole()).isEqualTo(expectedOrder.getCustomer().getUser().getRole());
                    Assertions.assertThat(actualOrder.getCustomer().getUser().getAuthority()).isEqualTo(expectedOrder.getCustomer().getUser().getAuthority());
                    Assertions.assertThat(actualOrder.getCustomer().getUser().getPhone()).isEqualTo(expectedOrder.getCustomer().getUser().getPhone());
                    Assertions.assertThat(actualOrder.getCustomer().getUser().getAddress().getZipCode()).isEqualTo(expectedOrder.getCustomer().getUser().getAddress().getZipCode());
                    Assertions.assertThat(actualOrder.getCustomer().getUser().getAddress().getAddress()).isEqualTo(expectedOrder.getCustomer().getUser().getAddress().getAddress());
                    Assertions.assertThat(actualOrder.getCustomer().getUser().getAddress().getDetailAddress()).isEqualTo(expectedOrder.getCustomer().getUser().getAddress().getDetailAddress());
                    Assertions.assertThat(actualOrder.getCustomer().getCompanyName()).isEqualTo(expectedOrder.getCustomer().getCompanyName());
                    Assertions.assertThat(actualOrder.getCustomer().getIsNew()).isEqualTo(expectedOrder.getCustomer().getIsNew());
                    Assertions.assertThat(actualOrder.getDeliveryAddress().getName()).isEqualTo(expectedOrder.getDeliveryAddress().getName());
                    Assertions.assertThat(actualOrder.getDeliveryAddress().getAddress().getZipCode()).isEqualTo(expectedOrder.getDeliveryAddress().getAddress().getZipCode());
                    Assertions.assertThat(actualOrder.getDeliveryAddress().getAddress().getAddress()).isEqualTo(expectedOrder.getDeliveryAddress().getAddress().getAddress());
                    Assertions.assertThat(actualOrder.getDeliveryAddress().getAddress().getDetailAddress()).isEqualTo(expectedOrder.getDeliveryAddress().getAddress().getDetailAddress());
                    Assertions.assertThat(actualOrder.getDeliveryAddress().getReceiver()).isEqualTo(expectedOrder.getDeliveryAddress().getReceiver());
                    Assertions.assertThat(actualOrder.getDeliveryAddress().getPhone1()).isEqualTo(expectedOrder.getDeliveryAddress().getPhone1());
                    Assertions.assertThat(actualOrder.getDeliveryAddress().getPhone2()).isEqualTo(expectedOrder.getDeliveryAddress().getPhone2());
                    Assertions.assertThat(actualOrder.getName()).isEqualTo(expectedOrder.getName());
                    Assertions.assertThat(actualOrder.getImgUrl()).isEqualTo(expectedOrder.getImgUrl());
                    Assertions.assertThat(actualOrder.getStage()).isEqualTo(expectedOrder.getStage());
                    Assertions.assertThat(actualOrder.getManufacturing().getIsLaserCutting()).isEqualTo(expectedOrder.getManufacturing().getIsLaserCutting());
                    Assertions.assertThat(actualOrder.getManufacturing().getIsBending()).isEqualTo(expectedOrder.getManufacturing().getIsBending());
                    Assertions.assertThat(actualOrder.getManufacturing().getIsWelding()).isEqualTo(expectedOrder.getManufacturing().getIsWelding());
                    Assertions.assertThat(actualOrder.getPostProcessing().getIsPainting()).isEqualTo(expectedOrder.getPostProcessing().getIsPainting());
                    Assertions.assertThat(actualOrder.getPostProcessing().getIsPlating()).isEqualTo(expectedOrder.getPostProcessing().getIsPlating());
                    Assertions.assertThat(actualOrder.getRequest()).isEqualTo(expectedOrder.getRequest());
                    Assertions.assertThat(actualOrder.getIsNewIssue()).isEqualTo(expectedOrder.getIsNewIssue());
                    Assertions.assertThat(actualOrder.getIsDeleted()).isEqualTo(expectedOrder.getIsDeleted());
                }
        );
    }

    @Test
    public void findFirstById_존재_X() {
        // given
        final Long unknownOrderId = 0L;

        // when
        Optional<Order> optionalOrder = orderRepository.findFirstById(unknownOrderId);

        // then
        Assertions.assertThat(optionalOrder.isEmpty()).isTrue();
    }

}
