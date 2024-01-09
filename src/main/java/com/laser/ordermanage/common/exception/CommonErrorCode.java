package com.laser.ordermanage.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    BAD_REQUEST("COMMON_400_01", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    INVALID_PARAMETER("COMMON_400_02", HttpStatus.BAD_REQUEST, " 파라미터가 올바르지 않습니다."),
    INVALID_PARAMETER_TYPE("COMMON_400_03", HttpStatus.BAD_REQUEST, " 파라미터의 타입이 올바르지 않습니다."),
    INVALID_FIELDS("COMMON_400_04", HttpStatus.BAD_REQUEST, ""),

    MISSING_COOKIE("COMMON_400_05", HttpStatus.BAD_REQUEST, " 쿠키값이 존재하지 않습니다."),
    MISSING_QUERY_PARAMETER("COMMON_400_06", HttpStatus.BAD_REQUEST, " 쿼리 파라미터 값이 존재하지 않습니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    UNAUTHORIZED("COMMON_401_01", HttpStatus.UNAUTHORIZED, "인증 자격 정보가 유효하지 않습니다."),

    // 403 FORBIDDEN 인증 필요
    FORBIDDEN("COMMON_403_01", HttpStatus.FORBIDDEN, "인증이 필요합니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND("COMMON_404_01", HttpStatus.NOT_FOUND, "리소스가 존재하지 않습니다."),

    // 405 METHOD_NOT_ALLOWED 허용하지 않은 Http Method
    METHOD_NOT_ALLOWED("COMMON_405_01", HttpStatus.METHOD_NOT_ALLOWED, "해당 요청에는 지원하지 않은 HTTP 메서드 입니다."),

    // 413 PAYLOAD_TOO_LARGE
    REQUEST_SIZE_EXCEEDED("COMMON_413_01", HttpStatus.PAYLOAD_TOO_LARGE, "요청의 크기가 100MB를 초과합니다."),
    REQUEST_FILE_SIZE_EXCEEDED("COMMON_413_02", HttpStatus.PAYLOAD_TOO_LARGE, "요청 파일의 크기가 100MB를 초과합니다."),

    // 500 INTERNAL SERVER ERROR 서버 에러
    INTERNAL_SERVER_ERROR("COMMON_500_01", HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다."),
    UNKNOWN_ERROR("COMMON_500_02", HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),
    UNABLE_TO_SEND_EMAIL("COMMON_500_03", HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송이 불가능합니다."),
    UNABLE_TO_AWS_S3_UPLOAD("COMMON_500_04", HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3 에 파일 업로드가 불가능합니다."),
    UNABLE_TO_EXTRACT_THUMBNAIL("COMMON_500_05", HttpStatus.INTERNAL_SERVER_ERROR, "썸네일 추출이 불가능합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
