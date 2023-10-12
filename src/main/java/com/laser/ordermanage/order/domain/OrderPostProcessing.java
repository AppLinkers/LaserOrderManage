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
public class OrderPostProcessing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isPainting;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isPlating;

}
