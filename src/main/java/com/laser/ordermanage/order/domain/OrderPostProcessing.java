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

    public List<String> toValueList() {
        List<String> valueList = new ArrayList<>();
        if (isPainting) {
            valueList.add("도색");
        }

        if (isPlating) {
            valueList.add("도금");
        }

        return valueList;
    }
}
