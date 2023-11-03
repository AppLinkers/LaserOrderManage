package com.laser.ordermanage.common.exception;

import com.laser.ordermanage.common.exception.dto.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomCommonException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String errorCode;

    public CustomCommonException(ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.httpStatus = errorcode.getHttpStatus();
        this.errorCode = errorcode.getCode();
    }

    public CustomCommonException(ErrorCode errorcode, String parameter) {
        super(parameter + errorcode.getMessage());
        this.httpStatus = errorcode.getHttpStatus();
        this.errorCode = errorcode.getCode();
    }

    public ErrorResponse toErrorResponse() {
        return ErrorResponse.builder()
                .httpStatus(this.httpStatus)
                .errorCode(this.errorCode)
                .message(this.getMessage())
                .build();
    }
}
