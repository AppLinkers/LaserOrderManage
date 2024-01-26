package com.laser.ordermanage.order.repository;

import java.util.List;

public interface DrawingRepositoryCustom {
    void deleteAllByOrder(Long orderId);

    void deleteAllByOrderList(List<Long> orderIdList);
}
