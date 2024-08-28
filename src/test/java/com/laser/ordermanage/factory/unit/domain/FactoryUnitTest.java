package com.laser.ordermanage.factory.unit.domain;

import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.domain.FactoryBuilder;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequest;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequestBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class FactoryUnitTest {

    @Test
    public void updateProperties() {
        // given
        final Factory factory = FactoryBuilder.build();
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.build();

        // then
        factory.updateProperties(request);

        // then
        Assertions.assertThat(factory.getCompanyName()).isEqualTo(request.companyName());
        Assertions.assertThat(factory.getRepresentative()).isEqualTo(request.representative());
        Assertions.assertThat(factory.getFax()).isEqualTo(request.fax());
    }
}
