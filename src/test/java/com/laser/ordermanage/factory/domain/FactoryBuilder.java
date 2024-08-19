package com.laser.ordermanage.factory.domain;

public class FactoryBuilder {
    public static Factory build() {
        return Factory.builder()
                .companyName("금오 M.T")
                .representative("정연근")
                .fax("0314310413")
                .build();
    }
}
