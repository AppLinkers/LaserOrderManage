package com.laser.ordermanage.order.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Ingredient {
    SS_330("SS 330");

    @Getter
    private final String name;
}
