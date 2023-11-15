package com.laser.ordermanage.order.service;

import com.laser.ordermanage.customer.dto.response.CustomerGetDrawingResponse;
import com.laser.ordermanage.order.repository.DrawingRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DrawingService {

    private final DrawingRepositoryCustom drawingRepository;

    @Transactional(readOnly = true)
    public List<CustomerGetDrawingResponse> getDrawingByCustomerAndOrder(String userName, Long orderId) {
        return drawingRepository.findByCustomerAndOrder(userName, orderId);
    }
}
