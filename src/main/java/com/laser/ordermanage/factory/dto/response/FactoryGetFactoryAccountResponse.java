package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.factory.domain.Factory;
import lombok.Builder;

@Builder
public record FactoryGetFactoryAccountResponse(
        String companyName,
        String representative,
        String fax
) {
    public static FactoryGetFactoryAccountResponse fromEntity(Factory factory) {
        return FactoryGetFactoryAccountResponse.builder()
                .companyName(factory.getCompanyName())
                .representative(factory.getRepresentative())
                .fax(factory.getFax())
                .build();
    }
}
