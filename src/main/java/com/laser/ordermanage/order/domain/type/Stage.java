package com.laser.ordermanage.order.domain.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Stage {
    NEW("견적 대기"),
    QUOTE_APPROVAL("견적 승인"),
    IN_PRODUCTION("제작 중"),
    SHIPPING("배송 중"),
    COMPLETED("거래 완료");

    private final String value;

}
