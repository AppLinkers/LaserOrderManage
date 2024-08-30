package com.laser.ordermanage.factory.dto.request;

public class FactoryCreateOrderAcquirerRequestBuilder {
    public static FactoryCreateOrderAcquirerRequest build() {
        return new FactoryCreateOrderAcquirerRequest("인수자 이름", "01011111111");
    }
}
