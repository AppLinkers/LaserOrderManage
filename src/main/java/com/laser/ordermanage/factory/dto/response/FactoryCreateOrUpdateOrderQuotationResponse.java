package com.laser.ordermanage.factory.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FactoryCreateOrUpdateOrderQuotationResponse {

    private final Long id;

    private final String fileName;

    private final String fileUrl;

    @Builder
    public FactoryCreateOrUpdateOrderQuotationResponse(Long id, String fileName, String fileUrl) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }
}
