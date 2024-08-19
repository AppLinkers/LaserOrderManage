package com.laser.ordermanage.factory.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum FactoryErrorCode implements ErrorCode {

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_FACTORY("FACTORY_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 공장 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
