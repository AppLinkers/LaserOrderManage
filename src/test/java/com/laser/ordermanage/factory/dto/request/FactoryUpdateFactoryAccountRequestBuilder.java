package com.laser.ordermanage.factory.dto.request;

public class FactoryUpdateFactoryAccountRequestBuilder {

    public static FactoryUpdateFactoryAccountRequest build() {
        return new FactoryUpdateFactoryAccountRequest("공장 수정 상호", "공장 수정 대표자", "03111111111");
    }

    public static FactoryUpdateFactoryAccountRequest nullCompanyNameBuild() {
        return new FactoryUpdateFactoryAccountRequest(null, "공장 수정 대표자", "03111111111");
    }

    public static FactoryUpdateFactoryAccountRequest emptyCompanyNameBuild() {
        return new FactoryUpdateFactoryAccountRequest("", "공장 수정 대표자", "03111111111");
    }

    public static FactoryUpdateFactoryAccountRequest invalidCompanyNameBuild() {
        return new FactoryUpdateFactoryAccountRequest("공장 수정 상호".repeat(3), "공장 수정 대표자", "03111111111");
    }

    public static FactoryUpdateFactoryAccountRequest nullCompanyRepresentativeBuild() {
        return new FactoryUpdateFactoryAccountRequest("공장 수정 상호", null, "03111111111");
    }

    public static FactoryUpdateFactoryAccountRequest emptyCompanyRepresentativeBuild() {
        return new FactoryUpdateFactoryAccountRequest("공장 수정 상호", "", "03111111111");
    }

    public static FactoryUpdateFactoryAccountRequest invalidCompanyRepresentativeBuild() {
        return new FactoryUpdateFactoryAccountRequest("공장 수정 상호", "공장 수정 대표자".repeat(2), "03111111111");
    }

    public static FactoryUpdateFactoryAccountRequest nullCompanyFaxBuild() {
        return new FactoryUpdateFactoryAccountRequest("공장 수정 상호", "공장 수정 대표자", null);
    }

    public static FactoryUpdateFactoryAccountRequest invalidCompanyFaxBuild() {
        return new FactoryUpdateFactoryAccountRequest("공장 수정 상호", "공장 수정 대표자", "invalid-fax");
    }
}
