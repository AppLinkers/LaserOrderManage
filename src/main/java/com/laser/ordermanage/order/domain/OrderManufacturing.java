package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
