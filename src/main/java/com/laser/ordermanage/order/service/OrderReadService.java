package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.dto.response.PageRes;
import com.laser.ordermanage.customer.dto.response.GetOrderRes;
import com.laser.ordermanage.factory.dto.response.GetOrderReIssueRes;
import com.laser.ordermanage.order.repository.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderReadService {

    private final OrderRepositoryCustom orderRepositoryCustom;

    @Transactional(readOnly = true)
    public PageRes<GetOrderRes> readByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query) {
        Page<GetOrderRes> orderPage = orderRepositoryCustom.findByCustomer(userName, pageable, stageRequestList, manufacturingRequestList, query);

        return new PageRes<>(orderPage);
    }

    public PageRes<GetOrderReIssueRes> readNewReIssueByFactory(String userName, Pageable pageable, Boolean hasQuotation, Boolean isUrgent) {
        Page<GetOrderReIssueRes> orderReIssuePage = orderRepositoryCustom.findNewReIssueByFactory(userName, pageable, hasQuotation, isUrgent);

        return new PageRes<>(orderReIssuePage);
    }
}
