package com.laser.ordermanage.common.exception;

import com.laser.ordermanage.common.exception.dto.response.ErrorRes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomCommonException extends RuntimeException{

    private final HttpStatus httpStatus;

    public CustomCommonException(ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.httpStatus = errorcode.getHttpStatus();
    }

    public ErrorRes toErrorRes() {
        return ErrorRes.builder()
                .httpStatus(this.httpStatus)
                .message(this.getMessage())
                .build();
    }
}
