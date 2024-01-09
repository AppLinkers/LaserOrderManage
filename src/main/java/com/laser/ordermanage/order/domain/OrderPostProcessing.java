package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_post_processing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderPostProcessing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_painting", nullable = false, length = 1)
    private Boolean isPainting = Boolean.FALSE;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_plating", nullable = false, length = 1)
    private Boolean isPlating = Boolean.FALSE;

    public List<String> toValueList() {
        List<String> valueList = new ArrayList<>();
        if (isPainting) {
            valueList.add("painting");
        }

        if (isPlating) {
            valueList.add("plating");
        }

        return valueList;
    }

    public static OrderPostProcessing ofRequest(List<String> requestList) {
        OrderPostProcessing orderPostProcessing = new OrderPostProcessing();
        requestList.forEach(
                request -> {
                    switch (request) {
                        case "painting" -> orderPostProcessing.isPainting = true;
                        case "plating" -> orderPostProcessing.isPlating = true;
                        default -> throw new CustomCommonException(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, "postProcessing 의 타입이 옳바르지 않습니다.");
                    }
                }
        );

        return orderPostProcessing;
    }
}
