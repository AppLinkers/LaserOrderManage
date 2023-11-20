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
public class OrderManufacturing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isLaserCutting = Boolean.FALSE;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isBending = Boolean.FALSE;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isWeldingFabrication = Boolean.FALSE;

    public List<String> toValueList() {
        List<String> valueList = new ArrayList<>();
        if (isLaserCutting) {
            valueList.add("레이저 가공");
        }

        if (isBending) {
            valueList.add("절곡");
        }

        if (isWeldingFabrication) {
            valueList.add("용접 및 제작");
        }

        return valueList;
    }

    public static OrderManufacturing ofRequest(List<String> requestList) {
        OrderManufacturing orderManufacturing = new OrderManufacturing();
        requestList.forEach(
                request -> {
                    switch (request) {
                        case "laser-cutting" -> orderManufacturing.isLaserCutting = Boolean.TRUE;
                        case "bending" -> orderManufacturing.isBending = Boolean.TRUE;
                        case "welding-fabrication" -> orderManufacturing.isWeldingFabrication = Boolean.TRUE;
                        default -> throw new CustomCommonException(ErrorCode.INVALID_FIELDS, "manufacturing 의 타입이 옳바르지 않습니다.");
                    }
                }
        );

        return orderManufacturing;
    }

}
