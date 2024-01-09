package com.laser.ordermanage.factory.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum FactoryErrorCode implements ErrorCode {

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS_TO_ORDER_MANAGER("FACTORY_403_01", HttpStatus.FORBIDDEN, "거래 관리자에 대한 접근 권한이 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
