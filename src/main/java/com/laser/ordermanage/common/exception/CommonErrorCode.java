package com.laser.ordermanage.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    BAD_REQUEST("-000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_JWT_TOKEN("-001", HttpStatus.BAD_REQUEST, "JWT 토큰 정보가 요청에 포함되지 않았습니다."),

    INVALID_PARAMETER("-003", HttpStatus.BAD_REQUEST, " 파라미터가 올바르지 않습니다."),
    INVALID_PARAMETER_TYPE("-004", HttpStatus.BAD_REQUEST, " 파라미터의 타입이 올바르지 않습니다."),
    INVALID_FIELDS("-005", HttpStatus.BAD_REQUEST, ""),

    MISSING_COOKIE("-006", HttpStatus.BAD_REQUEST, " 쿠키값이 존재하지 않습니다."),
    MISSING_QUERY_PARAMETER("-007", HttpStatus.BAD_REQUEST, " 쿼리 파라미터 값이 존재하지 않습니다."),

    REQUEST_FILE_SIZE_EXCEED("-008", HttpStatus.BAD_REQUEST, "요청 파일의 크기가 100MB를 초과합니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    UNAUTHORIZED("-100", HttpStatus.UNAUTHORIZED, "인증 자격 정보가 유효하지 않습니다."),

    // 403 FORBIDDEN 인증 필요
    FORBIDDEN("-300", HttpStatus.FORBIDDEN, "인증이 필요합니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND("-400", HttpStatus.NOT_FOUND, "리소스가 존재하지 않습니다."),
    NOT_FOUND_ENTITY("-402", HttpStatus.NOT_FOUND, " 엔티티가 존재하지 않습니다."),

    // 405 METHOD_NOT_ALLOWED 허용하지 않은 Http Method
    METHOD_NOT_ALLOWED("-405", HttpStatus.METHOD_NOT_ALLOWED, "해당 요청에는 지원하지 않은 HTTP 메서드 입니다."),

    // 500 INTERNAL SERVER ERROR 서버 에러
    INTERNAL_SERVER_ERROR("-500", HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다."),
    UNKNOWN_ERROR("-501", HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),
    UNABLE_TO_SEND_EMAIL("-502", HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송이 불가능합니다."),
    UNABLE_TO_AWS_S3_UPLOAD("-503", HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3 에 파일 업로드가 불가능합니다."),
    UNABLE_TO_EXTRACT_THUMBNAIL("-504", HttpStatus.INTERNAL_SERVER_ERROR, "썸네일 추출이 불가능합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
