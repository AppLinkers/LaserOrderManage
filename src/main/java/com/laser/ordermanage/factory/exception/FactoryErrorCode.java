package com.laser.ordermanage.factory.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum FactoryErrorCode implements ErrorCode {

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS_TO_ORDER_MANAGER("FACTORY_403_01", HttpStatus.FORBIDDEN, "거래 담당자에 대한 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_ORDER_MANAGER("FACTORY_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 거래 담당자 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
