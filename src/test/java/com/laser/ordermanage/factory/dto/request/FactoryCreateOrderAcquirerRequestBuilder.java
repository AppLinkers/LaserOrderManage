package com.laser.ordermanage.factory.dto.request;

public class FactoryCreateOrderAcquirerRequestBuilder {
    public static FactoryCreateOrderAcquirerRequest build() {
        return new FactoryCreateOrderAcquirerRequest("인수자 이름", "01011111111");
    }

    public static FactoryCreateOrderAcquirerRequest nullNameBuild() {
        return new FactoryCreateOrderAcquirerRequest(null, "01011111111");
    }

    public static FactoryCreateOrderAcquirerRequest emptyNameBuild() {
        return new FactoryCreateOrderAcquirerRequest("", "01011111111");
    }

    public static FactoryCreateOrderAcquirerRequest invalidNameBuild() {
        return new FactoryCreateOrderAcquirerRequest("인수자 이름".repeat(2), "01011111111");
    }

    public static FactoryCreateOrderAcquirerRequest nullPhoneBuild() {
        return new FactoryCreateOrderAcquirerRequest("인수자 이름", null);
    }

    public static FactoryCreateOrderAcquirerRequest invalidPhoneBuild() {
        return new FactoryCreateOrderAcquirerRequest("인수자 이름", "invalid-phone");
    }
}
