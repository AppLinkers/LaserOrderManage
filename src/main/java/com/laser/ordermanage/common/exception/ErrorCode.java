package com.laser.ordermanage.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_USER_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT Token 정보가 유효하지 않습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "Access Token 정보가 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token 정보가 유효하지 않습니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_AUTHENTICATION(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않은 사용자 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
