package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
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
    private Boolean isLaserCutting;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isBending;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isWeldingFabrication;

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

}
