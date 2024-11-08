package com.laser.ordermanage.order.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.dto.response.*;
import com.laser.ordermanage.factory.dto.response.*;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderBuilder;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponseBuilder;
import com.laser.ordermanage.order.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = OrderRepository.class)
public class OrderRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private OrderRepository orderRepository;

    private final static Pageable pageable = PageRequest.of(0, 10);

    @Test
    public void findFirstById_존재_O() {
        // given
        final Long expectedOrderId = 1L;
        final Order expectedOrder = OrderBuilder.build();
        expectedOrder.changeStageToCompleted();

        // when
        final Optional<Order> optionalOrder = orderRepository.findFirstById(expectedOrderId);

        // then
        Assertions.assertThat(optionalOrder.isPresent()).isTrue();
        optionalOrder.ifPresent(
                actualOrder -> {
                    Assertions.assertThat(actualOrder.getId()).isEqualTo(expectedOrderId);
                    OrderBuilder.assertOrder(actualOrder, expectedOrder);
                }
        );
    }

    @Test
    public void findFirstById_존재_X() {
        // given
        final Long unknownOrderId = 0L;

        // when
        final Optional<Order> optionalOrder = orderRepository.findFirstById(unknownOrderId);

        // then
        Assertions.assertThat(optionalOrder.isEmpty()).isTrue();
    }

    @Test
    public void findByCustomer() {
        // given
        final String email = "user1@gmail.com";
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1();

        // when
        final Page<CustomerGetOrderHistoryResponse> actualResponse = orderRepository.findByCustomer(email, pageable, null, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByCustomer_stageIsCompleted() {
        // given
        final String email = "user1@gmail.com";
        final List<String> stageRequestList = List.of(Stage.COMPLETED.getRequest());
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1AndStageIsCompleted();

        // when
        final Page<CustomerGetOrderHistoryResponse> actualResponse = orderRepository.findByCustomer(email, pageable, stageRequestList, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByCustomer_실패_Stage() {
        // given
        final String email = "user1@gmail.com";
        final List<String> invalidStageRequestList = List.of("invalid-stage");

        // when & then
        Assertions.assertThatThrownBy(() -> orderRepository.findByCustomer(email, pageable, invalidStageRequestList, null, null))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("stage 파라미터가 올바르지 않습니다.");
    }

    @Test
    public void findByCustomer_manufacturingIsLaserCutting() {
        // given
        final String email = "user1@gmail.com";
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1AndManufacturingIsLaserCutting();
        final List<String> manufacturingRequestList = List.of("laser-cutting");

        // when
        final Page<CustomerGetOrderHistoryResponse> actualResponse = orderRepository.findByCustomer(email, pageable, null, manufacturingRequestList, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByCustomer_manufacturingIsWelding() {
        // given
        final String email = "user1@gmail.com";
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1AndManufacturingIsWelding();
        final List<String> manufacturingRequestList = List.of("welding");

        // when
        final Page<CustomerGetOrderHistoryResponse> actualResponse = orderRepository.findByCustomer(email, pageable, null, manufacturingRequestList, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByCustomer_manufacturingIsBending() {
        // given
        final String email = "user1@gmail.com";
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1();
        final List<String> manufacturingRequestList = List.of("bending");

        // when
        final Page<CustomerGetOrderHistoryResponse> actualResponse = orderRepository.findByCustomer(email, pageable, null, manufacturingRequestList, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByCustomer_실패_manufacturing() {
        // given
        final String email = "user1@gmail.com";
        final List<String> invalidManufacturingRequestList = List.of("invalid-manufacturing");

        // when & then
        Assertions.assertThatThrownBy(() -> orderRepository.findByCustomer(email, pageable, null, invalidManufacturingRequestList, null))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("manufacturing 파라미터가 올바르지 않습니다.");
    }

    @Test
    public void findByCustomer_query() {
        // given
        final String email = "user1@gmail.com";
        final String query = "1 이름";
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1AndStageIsCompleted();

        // when
        final Page<CustomerGetOrderHistoryResponse> actualResponse = orderRepository.findByCustomer(email, pageable, null, null, query);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsReIssueByFactory() {
        // given
        final List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder.build();

        // when
        final Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsReIssueByFactory(pageable, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsReIssueByFactory_hasQuotation_True() {
        // given
        final List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder.buildOfHasQuotationIsTrueAndIsUrgentIsTrue();

        // when
        final Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsReIssueByFactory(pageable, Boolean.TRUE, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsReIssueByFactory_hasQuotation_False() {
        // given
        final List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder.buildOfHasQuotationIsFalseAndIsUrgentIsFalse();

        // when
        final Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsReIssueByFactory(pageable, Boolean.FALSE, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsReIssueByFactory_isUrgent_True() {
        // given
        final List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder.buildOfHasQuotationIsTrueAndIsUrgentIsTrue();

        // when
        final Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsReIssueByFactory(pageable, null, Boolean.TRUE);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsReIssueByFactory_isUrgent_False() {
        // given
        final List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder.buildOfHasQuotationIsFalseAndIsUrgentIsFalse();

        // when
        final Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsReIssueByFactory(pageable, null, Boolean.FALSE);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsNewIssueByFactory() {
        // given
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.build();

        // when
        final Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, null, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsNewIssueByFactory_hasQuotation_True() {
        // given
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.buildOfHasQuotationIsTrue();

        // when
        final Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, Boolean.TRUE, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsNewIssueByFactory_hasQuotation_False() {
        // given
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.buildOfHasQuotationIsFalse();

        // when
        final Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, Boolean.FALSE, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsNewIssueByFactory_isNewCustomer_True() {
        // given
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.buildOfIsNewCustomerIsTrue();

        // when
        final Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, null, Boolean.TRUE, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsNewIssueByFactory_isNewCustomer_False() {
        // given
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.buildOfIsNewCustomerIsFalse();

        // when
        final Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, null, Boolean.FALSE, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsNewIssueByFactory_isUrgent_True() {
        // given
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.buildOfIsUrgentIsTrue();

        // when
        final Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, null, null, Boolean.TRUE);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsNewAndIsNewIssueByFactory_isUrgent_False() {
        // given
        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.buildOfIsUrgentIsFalse();

        // when
        final Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, null, null, Boolean.FALSE);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByFactory_isCompleted_False() {
        // given
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedFalseAndPage1();

        // when
        final Page<FactoryGetOrderHistoryResponse> actualResponse = orderRepository.findByFactory(pageable, Boolean.FALSE, null, null, null, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByFactory_isCompleted_True() {
        // given
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedTrue();

        // when
        final Page<FactoryGetOrderHistoryResponse> actualResponse = orderRepository.findByFactory(pageable, Boolean.TRUE, null, null, null, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }


    @Test
    public void findByFactory_isCompleted_False_isUrgent_True() {
        // given
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedFalseAndIsUrgentTrue();

        // when
        final Page<FactoryGetOrderHistoryResponse> actualResponse = orderRepository.findByFactory(pageable, Boolean.FALSE, Boolean.TRUE, null, null, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByFactory_isCompleted_False_isUrgent_False() {
        // given
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedFalseAndIsUrgentFalse();

        // when
        final Page<FactoryGetOrderHistoryResponse> actualResponse = orderRepository.findByFactory(pageable, Boolean.FALSE, Boolean.FALSE, null, null, null, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByFactory_isCompleted_False_dateCriterion_create() {
        // given
        final String dateCriterion = "create";
        final LocalDate startDate = LocalDate.of(2023, 10, 16);
        final LocalDate endDate = LocalDate.of(2023, 10, 24);
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedFalseAndDateCriterionStart();

        // when
        final Page<FactoryGetOrderHistoryResponse> actualResponse = orderRepository.findByFactory(pageable, Boolean.FALSE, null, dateCriterion, startDate, endDate, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByFactory_isCompleted_False_dateCriterion_delivery() {
        // given
        final String dateCriterion = "delivery";
        final LocalDate startDate = LocalDate.of(2023, 10, 28);
        final LocalDate endDate = LocalDate.of(2023, 11, 1);
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedFalseAndDateCriterionDelivery();

        // when
        final Page<FactoryGetOrderHistoryResponse> actualResponse = orderRepository.findByFactory(pageable, Boolean.FALSE, null, dateCriterion, startDate, endDate, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findByFactory_실패_dateCriterion() {
        // given
        final String invalidDateCriterion = "invalid-date-criterion";
        final LocalDate startDate = LocalDate.of(2023, 10, 28);
        final LocalDate endDate = LocalDate.of(2023, 11, 1);

        // when & then
        Assertions.assertThatThrownBy(() -> orderRepository.findByFactory(pageable, Boolean.FALSE, null, invalidDateCriterion, startDate, endDate, null))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("date-criterion 파라미터가 올바르지 않습니다.");
    }

    @Test
    public void findByFactory_isCompleted_False_query() {
        // given
        final String query = "거래 4 이름";
        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedFalseAndQuery();

        // when
        final Page<FactoryGetOrderHistoryResponse> actualResponse = orderRepository.findByFactory(pageable, Boolean.FALSE, null, null, null, null, query);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsCompletedByCustomer() {
        // given
        final String email = "user1@gmail.com";
        final List<CustomerGetOrderIsCompletedHistoryResponse> expectedOrderList = CustomerGetOrderIsCompletedHistoryResponseBuilder.build();

        // when
        final Page<CustomerGetOrderIsCompletedHistoryResponse> actualResponse = orderRepository.findIsCompletedByCustomer(email, pageable, null);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findIsCompletedByCustomer_query() {
        // given
        final String email = "user1@gmail.com";
        final String query = "거래 1 이름";
        final List<CustomerGetOrderIsCompletedHistoryResponse> expectedOrderList = CustomerGetOrderIsCompletedHistoryResponseBuilder.build();

        // when
        final Page<CustomerGetOrderIsCompletedHistoryResponse> actualResponse = orderRepository.findIsCompletedByCustomer(email, pageable, query);

        // then
        assertPage(pageable, expectedOrderList, actualResponse);
    }

    @Test
    public void findCreateInformationByOrder() {
        // given
        final Long orderId = 1L;
        final CustomerGetOrderCreateInformationResponse expectedResponse = CustomerGetOrderCreateInformationResponseBuilder.build();

        // when
        final CustomerGetOrderCreateInformationResponse actualResponse = orderRepository.findCreateInformationByOrder(orderId);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void findDetailByOrder() {
        // given
        final Long orderId = 1L;
        final GetOrderDetailResponse expectedResponse = GetOrderDetailResponseBuilder.build();

        // when
        final Optional<GetOrderDetailResponse> optionalActualResponse = orderRepository.findDetailByOrder(orderId);

        // then
        Assertions.assertThat(optionalActualResponse.isPresent()).isTrue();
        optionalActualResponse.ifPresent(
                actualResponse -> Assertions.assertThat(actualResponse).isEqualTo(expectedResponse)
        );
    }

    @Test
    public void findDetailByOrder_존재_X() {
        // given
        final Long unknownOrderId = 0L;

        // when
        final Optional<GetOrderDetailResponse> optionalActualResponse = orderRepository.findDetailByOrder(unknownOrderId);

        // then
        Assertions.assertThat(optionalActualResponse.isEmpty()).isTrue();
    }

    @Test
    public void findUserEmailById() {
        // given
        final Long orderId = 1L;
        final String expectedResponse = "user1@gmail.com";

        // when
        final Optional<String> optionalActualResponse = orderRepository.findUserEmailById(orderId);

        // then
        Assertions.assertThat(optionalActualResponse.isPresent()).isTrue();
        optionalActualResponse.ifPresent(
                actualResponse -> Assertions.assertThat(actualResponse).isEqualTo(expectedResponse)
        );
    }

    @Test
    public void findUserEmailById_존재_X() {
        // given
        final Long unknownOrderId = 0L;

        // when
        final Optional<String> optionalActualResponse = orderRepository.findUserEmailById(unknownOrderId);

        // then
        Assertions.assertThat(optionalActualResponse.isEmpty()).isTrue();
    }

    @Test
    public void findByCustomerAndStageCompleted() {
        // given
        final String email = "user1@gmail.com";
        final Long expectedOrderId = 1L;
        final Order expectedOrder = OrderBuilder.build();
        expectedOrder.changeStageToCompleted();

        // when
        final List<Order> actualResponse = orderRepository.findByCustomerAndStageCompleted(email);

        // then
        Assertions.assertThat(actualResponse.size()).isEqualTo(1);
        final Order actualOrder = actualResponse.get(0);
        Assertions.assertThat(actualOrder.getId()).isEqualTo(expectedOrderId);
        OrderBuilder.assertOrder(actualOrder, expectedOrder);
    }

    @Test
    public void findIdByCustomerAndStageNotCompleted() {
        // given
        final String email = "user1@gmail.com";
        List<Long> expectedResponse = List.of(2L, 3L, 4L, 5L);

        // when
        List<Long> actualResponse = orderRepository.findIdByCustomerAndStageNotCompleted(email);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
