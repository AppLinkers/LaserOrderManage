package com.laser.ordermanage.customer.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CustomerErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    DEFAULT_DELIVERY_ADDRESS_DELETE("CUSTOMER_400_01", HttpStatus.BAD_REQUEST, "기본 배송지는 삭제할 수 없습니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS_TO_DELIVERY_ADDRESS("CUSTOMER_403_01", HttpStatus.FORBIDDEN, "배송지에 대한 접근 권한이 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
