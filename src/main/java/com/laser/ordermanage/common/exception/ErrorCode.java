package com.laser.ordermanage.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    BAD_REQUEST("-000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_JWT_TOKEN("-001", HttpStatus.BAD_REQUEST, "JWT 토큰 정보가 요청에 포함되지 않았습니다."),
    INVALID_CREDENTIALS("-002", HttpStatus.BAD_REQUEST, "ID 또는 비밀번호가 올바르지 않습니다."),

    INVALID_PARAMETER("-003", HttpStatus.BAD_REQUEST, " 파라미터가 올바르지 않습니다."),
    INVALID_PARAMETER_TYPE("-004", HttpStatus.BAD_REQUEST, " 파라미터의 타입이 올바르지 않습니다."),
    INVALID_FIELDS("-005", HttpStatus.BAD_REQUEST, ""),

    MISSING_COOKIE("-006", HttpStatus.BAD_REQUEST, " 쿠키값이 존재하지 않습니다."),
    MISSING_QUERY_PARAMETER("-007", HttpStatus.BAD_REQUEST, " 쿼리 파라미터 값이 존재하지 않습니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    UNAUTHORIZED("-100", HttpStatus.UNAUTHORIZED, "인증 자격 정보가 유효하지 않습니다."),
    INVALID_JWT_TOKEN("-101", HttpStatus.UNAUTHORIZED, "JWT Token 정보가 유효하지 않습니다."),
    EXPIRED_JWT_TOKEN("-102", HttpStatus.UNAUTHORIZED, "JWT 토큰 정보가 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN("-103", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰 입니다."),
    UNAUTHORIZED_JWT_TOKEN("-104", HttpStatus.UNAUTHORIZED, "JWT 토큰에 권한정보가 존재하지 않습니다."),

    INVALID_ACCESS_JWT_TOKEN("-105", HttpStatus.UNAUTHORIZED, "Access JWT 토큰 정보가 유효하지 않습니다."),
    INVALID_REFRESH_JWT_TOKEN("-106", HttpStatus.UNAUTHORIZED, "Refresh JWT 토큰 정보가 유효하지 않습니다."),

    INVALID_VERIFY_CODE("-107", HttpStatus.UNAUTHORIZED, "인증 코드 정보가 유효하지 않습니다."),

    // 403 FORBIDDEN 인증 필요
    FORBIDDEN("-300", HttpStatus.FORBIDDEN, "인증이 필요합니다."),
    DENIED_ACCESS("-301", HttpStatus.FORBIDDEN, "해당 요청에 대한 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND("-400", HttpStatus.NOT_FOUND, "리소스가 존재하지 않습니다."),
    NOT_FOUND_VERIFY_CODE("-401", HttpStatus.NOT_FOUND, "이메일에 해당하는 인증 코드가 존재하지 않습니다."),

    // 500 INTERNAL SERVER ERROR 서버 에러
    INTERNAL_SERVER_ERROR("-500", HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다."),
    UNKNOWN_ERROR("-501", HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),
    UNABLE_TO_SEND_EMAIL("-502", HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송이 불가능합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
