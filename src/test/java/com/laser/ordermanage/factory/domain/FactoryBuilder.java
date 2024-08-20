package com.laser.ordermanage.factory.domain;

import org.assertj.core.api.Assertions;

public class FactoryBuilder {
    public static Factory build() {
        return Factory.builder()
                .companyName("금오 M.T")
                .representative("정연근")
                .fax("0314310413")
                .build();
    }

    public static void assertFactory(Factory actualFactory, Factory expectedFactory) {
        Assertions.assertThat(actualFactory.getCompanyName()).isEqualTo(expectedFactory.getCompanyName());
        Assertions.assertThat(actualFactory.getRepresentative()).isEqualTo(expectedFactory.getRepresentative());
        Assertions.assertThat(actualFactory.getFax()).isEqualTo(expectedFactory.getFax());
    }
}
