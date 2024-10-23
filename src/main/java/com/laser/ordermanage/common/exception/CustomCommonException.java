package com.laser.ordermanage.common.exception;

import com.laser.ordermanage.common.exception.dto.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        super(String.format(errorcode.getMessage(), parameter));
        this.httpStatus = errorcode.getHttpStatus();
        this.errorCode = errorcode.getCode();
    }

    public ResponseEntity<ErrorResponse> toErrorResponse() {
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(this.errorCode)
                .message(this.getMessage())
                .build();

        return ResponseEntity.status(this.httpStatus).body(response);
    }
}
