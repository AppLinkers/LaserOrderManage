package com.laser.ordermanage.factory.dto.response;

import lombok.Builder;

@Builder
public record FactoryGetFactoryAccountResponse(
        String companyName,
        String representative,
        String fax
) { }
