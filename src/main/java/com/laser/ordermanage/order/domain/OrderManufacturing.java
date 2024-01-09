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
@Table(name = "order_manufacturing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderManufacturing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_laser_cutting", nullable = false, length = 1)
    private Boolean isLaserCutting = Boolean.FALSE;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_bending", nullable = false, length = 1)
    private Boolean isBending = Boolean.FALSE;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_welding_fabrication", nullable = false, length = 1)
    private Boolean isWeldingFabrication = Boolean.FALSE;

    public List<String> toValueList() {
        List<String> valueList = new ArrayList<>();
        if (isLaserCutting) {
            valueList.add("laser-cutting");
        }

        if (isBending) {
            valueList.add("bending");
        }

        if (isWeldingFabrication) {
            valueList.add("welding-fabrication");
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
                        default -> throw new CustomCommonException(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, "manufacturing 의 타입이 옳바르지 않습니다.");
                    }
                }
        );

        return orderManufacturing;
    }

}
