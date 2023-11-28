package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderPostProcessing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isPainting = Boolean.FALSE;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
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
                        default -> throw new CustomCommonException(ErrorCode.INVALID_FIELDS, "postProcessing 의 타입이 옳바르지 않습니다.");
                    }
                }
        );

        return orderPostProcessing;
    }
}
