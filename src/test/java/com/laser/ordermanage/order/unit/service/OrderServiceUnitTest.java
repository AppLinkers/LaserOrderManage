package com.laser.ordermanage.order.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.order.domain.Comment;
import com.laser.ordermanage.order.domain.CommentBuilder;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderBuilder;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.request.CreateCommentRequestBuilder;
import com.laser.ordermanage.order.dto.response.*;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.CommentRepository;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.order.service.OrderService;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.service.UserAuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class OrderServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private DrawingRepository drawingRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserAuthService userAuthService;

    /**
     * 거래 DB id 기준으로 거래 조회 성공
     */
    @Test
    public void getOrderById_성공() {
        // given
        final Long orderId = 1L;
        final Order expectedOrder = OrderBuilder.build();

        // stub
        when(orderRepository.findFirstById(orderId)).thenReturn(Optional.of(expectedOrder));

        // when
        final Order actualOrder = orderService.getOrderById(orderId);

        // then
        Assertions.assertThat(actualOrder).isEqualTo(expectedOrder);
    }

    /**
     * 거래 DB id 기준으로 거래 조회 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void getOrderById_실패_NOT_FOUND_ORDER() {
        // given
        final Long unknownOrderId = 0L;

        // stub
        when(orderRepository.findFirstById(unknownOrderId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.getOrderById(unknownOrderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_ORDER.getMessage());
    }

    /**
     * 거래 DB id 기준으로 고객 사용자 이메일 조회 성공
     */
    @Test
    public void getUserEmailByOrder_성공() {
        // given
        final Long orderId = 1L;
        final String expectedUserEmail = "user@gmail.com";

        // stub
        when(orderRepository.findUserEmailById(orderId)).thenReturn(Optional.of(expectedUserEmail));

        // when
        String actualUserEmail = orderService.getUserEmailByOrder(orderId);

        // then
        Assertions.assertThat(actualUserEmail).isEqualTo(expectedUserEmail);
    }

    /**
     * 거래 DB id 기준으로 고객 사용자 이메일 조회 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void getUserEmailByOrder_실패_NOT_FOUND_ORDER() {
        // given
        final Long unknownOrderId = 0L;

        // stub
        when(orderRepository.findUserEmailById(unknownOrderId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.getUserEmailByOrder(unknownOrderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_ORDER.getMessage());
    }

    /**
     * 거래 DB id 기준으로 거래 상세 정보 조회 성공
     */
    @Test
    public void getOrderDetail_성공() {
        // given
        final Long orderId = 1L;
        final GetOrderDetailResponse expectedResponse = GetOrderDetailResponseBuilder.build();

        // stub
        when(orderRepository.findDetailByOrder(orderId)).thenReturn(Optional.of(expectedResponse));

        // when
        final GetOrderDetailResponse actualResponse = orderService.getOrderDetail(orderId);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 DB id 기준으로 거래 상세 정보 조회 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void getOrderDetail_실패_NOT_FOUND_ORDER() {
        // given
        final Long unknownOrderId = 0L;

        // stub
        when(orderRepository.findDetailByOrder(unknownOrderId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.getOrderDetail(unknownOrderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_ORDER.getMessage());
    }

    /**
     * 거래 댓글 DB id 로 댓글 조회 성공
     */
    @Test
    public void getCommentByCommentId_성공() {
        // given
        final Long commentId = 1L;
        final Comment expectedComment = CommentBuilder.build();

        // stub
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(expectedComment));

        // when
        final Comment actualComment = orderService.getCommentByCommentId(commentId);

        // then
        Assertions.assertThat(actualComment).isEqualTo(expectedComment);
    }

    /**
     * 거래 댓글 DB id 로 댓글 조회 실패
     * - 실패 사유 : 존재하지 않는 댓글
     */
    @Test
    public void getCommentByCommentId_실패_NOT_FOUND_COMMENT() {
        // given
        final Long unknownCommentId = 0L;

        // stub
        when(commentRepository.findById(unknownCommentId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.getCommentByCommentId(unknownCommentId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_COMMENT.getMessage());
    }

    /**
     * 거래 DB id 로 댓글 목록 조회 성공
     */
    @Test
    public void getCommentByOrder_성공() {
        // given
        final Long orderId = 1L;
        final ListResponse<GetCommentResponse> expectedResponse = new ListResponse<>(GetCommentResponseBuilder.buildListForOrder1());

        // stub
        when(commentRepository.findCommentByOrder(orderId)).thenReturn(expectedResponse.contents());

        // when
        final ListResponse<GetCommentResponse> actualResponse = orderService.getCommentByOrder(orderId);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래에 댓글 작성 성공
     */
    @Test
    public void createOrderComment_성공() {
        // given
        final Comment expectedComment = CommentBuilder.build();
        final String email = expectedComment.getUser().getEmail();
        final Long orderId = 1L;
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // stub
        when(userAuthService.getUserByEmail(email)).thenReturn(expectedComment.getUser());
        when(orderRepository.findFirstById(orderId)).thenReturn(Optional.of(expectedComment.getOrder()));
        when(commentRepository.save(any())).thenReturn(expectedComment);

        // when
        final Long actualCommentId = orderService.createOrderComment(email, orderId, request);

        // then
        Assertions.assertThat(actualCommentId).isNull();
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void createOrderComment_실패_NOT_FOUND_ORDER() {
        // given
        final Comment expectedComment = CommentBuilder.build();
        final String email = expectedComment.getUser().getEmail();
        final Long unknownOrderId = 1L;
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // stub
        when(userAuthService.getUserByEmail(email)).thenReturn(expectedComment.getUser());
        when(orderRepository.findFirstById(unknownOrderId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.createOrderComment(email, unknownOrderId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_ORDER.getMessage());
    }

    /**
     * 거래 DB id 에 해당하는 거래 삭제 성공
     */
    @Test
    public void deleteOrder_성공() {
        // given
        final Long orderId = 1L;
        final Order order = OrderBuilder.build();
        final DeleteOrderResponse expectedResponse = DeleteOrderResponseBuilder.build();

        // stub
        when(orderRepository.findFirstById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(drawingRepository).deleteAllByOrder(orderId);
        doNothing().when(commentRepository).deleteAllByOrder(orderId);
        doNothing().when(orderRepository).delete(order);

        // when
        final DeleteOrderResponse actualResponse = orderService.deleteOrder(orderId);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 DB id 에 해당하는 거래 삭제 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void deleteOrder_실패_NOT_FOUND_ORDER() {
        // given
        final Long unknownOrderId = 0L;

        // stub
        when(orderRepository.findFirstById(unknownOrderId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.deleteOrder(unknownOrderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_ORDER.getMessage());
    }

    /**
     * 거래 DB id 에 해당하는 거래 삭제 실패
     * - 실패 사유 : 거래의 단계가 삭제 가능 단계(견적 대기, 견적 승인)가 아님
     */
    @Test
    public void deleteOrder_실패_INVALID_ORDER_STAGE() {
        // given
        final Long orderId = 1L;
        final Order order = OrderBuilder.build();
        order.changeStageToCompleted();

        // stub
        when(orderRepository.findFirstById(orderId)).thenReturn(Optional.of(order));

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.deleteOrder(orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(Stage.COMPLETED.getValue() + OrderErrorCode.INVALID_ORDER_STAGE.getMessage());
    }

    /**
     * 거래 DB id 에 해당하는 거래의 접근 권한 확인 성공 - 공장 사용자
     */
    @Test
    public void checkAuthorityCustomerOfOrderOrFactory_성공_Factory() {
        // given
        final UserEntity userEntity = UserEntityBuilder.factoryAdminUserBuild();
        final Long orderId = 1L;

        // stub
        when(userAuthService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);

        // when
        orderService.checkAuthorityCustomerOfOrderOrFactory(userEntity.getEmail(), orderId);
    }

    /**
     * 거래 DB id 에 해당하는 거래의 접근 권한 확인 성공 - 고객 사용자
     */
    @Test
    public void checkAuthorityCustomerOfOrderOrFactory_성공_Customer() {
        // given
        final UserEntity userEntity = UserEntityBuilder.build();
        final Long orderId = 1L;

        // stub
        when(userAuthService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(orderRepository.findUserEmailById(orderId)).thenReturn(Optional.of(userEntity.getEmail()));

        // when
        orderService.checkAuthorityCustomerOfOrderOrFactory(userEntity.getEmail(), orderId);
    }

    /**
     * 거래 DB id 에 해당하는 거래의 접근 권한 확인 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void checkAuthorityCustomerOfOrderOrFactory_실패_NOT_FOUND_ORDER() {
        // given
        final UserEntity userEntity = UserEntityBuilder.build();
        final Long unknownOrderId = 0L;

        // stub
        when(userAuthService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(orderRepository.findUserEmailById(unknownOrderId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.checkAuthorityCustomerOfOrderOrFactory(userEntity.getEmail(), unknownOrderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_ORDER.getMessage());
    }

    /**
     * 거래 DB id 에 해당하는 거래의 접근 권한 확인 실패
     * - 실패 사유 : 접근 권한 없음
     */
    @Test
    public void checkAuthorityCustomerOfOrderOrFactory_실패_DENIED_ACCESS_TO_ORDER() {
        // given
        final UserEntity userEntity = UserEntityBuilder.build();
        final Long orderId = 0L;
        final String userEmailOfOrder = "user-order@gmail.com";

        // stub
        when(userAuthService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(orderRepository.findUserEmailById(orderId)).thenReturn(Optional.of(userEmailOfOrder));

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.checkAuthorityCustomerOfOrderOrFactory(userEntity.getEmail(), orderId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.DENIED_ACCESS_TO_ORDER.getMessage());
    }

}
