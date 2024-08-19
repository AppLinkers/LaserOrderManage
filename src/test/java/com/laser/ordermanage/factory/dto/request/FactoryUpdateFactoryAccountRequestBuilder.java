package com.laser.ordermanage.factory.dto.request;

public class FactoryUpdateFactoryAccountRequestBuilder {

    public static FactoryUpdateFactoryAccountRequest build() {
        return new FactoryUpdateFactoryAccountRequest("공장 수정 상호", "공장 수정 대표자", "03111111111");
    }
}
