package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateOrderIsUrgentRequest;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FactoryOrderService {

    private final OrderService orderService;
    private final MailService mailService;

    @Transactional
    public void updateOrderIsUrgent(Long orderId, FactoryUpdateOrderIsUrgentRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableUpdateIsUrgent()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        order.updateIsUrgent(request.getIsUrgent());

        String toEmail = order.getCustomer().getUser().getEmail();

        StringBuilder sbTitle = new StringBuilder();
        StringBuilder sbContent = new StringBuilder();

        if (request.getIsUrgent()) {
            sbTitle.append("[거래 긴급 설정] ")
                    .append(order.getName())
                    .append(" 거래의 긴급이 설정 되었습니다.");

            sbContent.append("고객님, ")
                    .append(order.getName())
                    .append(" 거래의 긴급이 설정 되었습니다.");
        } else {
            sbTitle.append("[거래 긴급 설정 해제] ")
                    .append(order.getName())
                    .append(" 거래의 긴급 설정이 해제 되었습니다.");

            sbContent.append("고객님, ")
                    .append(order.getName())
                    .append(" 거래의 긴급 설정이 해제 되었습니다.");
        }

        String title = sbTitle.toString();
        String content = sbContent.toString();

        mailService.sendEmail(toEmail, title, content);
    }

}
