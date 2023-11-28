package com.laser.ordermanage.order.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum Ingredient {
    DOUBLE_HL("2HL"),
    DOUBLE_PL("2PL"),
    STS430("430"),
    AC("AC"),
    AL("AL"),
    ALCK("ALCK"),
    AR("AR"),
    ATOS("ATOS"),
    CK("CK"),
    CR("CR"),
    CU("CU"),
    EGI("EGI"),
    GI("GI"),
    HGI("HGI"),
    HL("HL"),
    MS("MS"),
    MS_N("MS-N"),
    PL("PL"),
    PO("PO"),
    S45("S45"),
    SM490("SM490"),
    SPCC("SPCC"),
    SS("SS"),
    SS400("SS400"),
    SS41("SS41"),
    STS("STS"),
    SUS("SUS"),
    SUSCK("SUSCK"),
    TI("TI");

    @Getter
    private final String value;

    private static final Map<String, Ingredient> valueMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(Ingredient::getValue, Function.identity())));

    public static Ingredient ofValue(String value) {
        return Optional.ofNullable(valueMap.get(value)).orElseThrow(() -> new CustomCommonException(ErrorCode.INVALID_INGREDIENT));
    }
}
