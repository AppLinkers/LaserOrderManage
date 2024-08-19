package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.domain.FactoryBuilder;

public class FactoryGetFactoryAccountResponseBuilder {
    public static FactoryGetFactoryAccountResponse build() {
        Factory factory = FactoryBuilder.build();

        return FactoryGetFactoryAccountResponse.builder()
                .companyName(factory.getCompanyName())
                .representative(factory.getRepresentative())
                .fax(factory.getFax())
                .build();
    }
}
