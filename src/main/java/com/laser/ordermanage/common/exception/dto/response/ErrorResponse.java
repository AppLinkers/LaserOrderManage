package com.laser.ordermanage.common.exception.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse (
        HttpStatus httpStatus,
        String errorCode,
        String message
) { }
