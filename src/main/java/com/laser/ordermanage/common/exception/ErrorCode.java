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

    REQUEST_FILE_SIZE_EXCEED("-008", HttpStatus.BAD_REQUEST, "요청 파일의 크기가 100MB를 초과합니다."),
    INVALID_FILE_EXTENSION("-009", HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    INVALID_INGREDIENT("-010", HttpStatus.BAD_REQUEST, "지원하지 않는 재료입니다."),
    INVALID_ORDER_STAGE("-011", HttpStatus.BAD_REQUEST, " 단계의 거래는 해당 요청을 수행할 수 없습니다."),
    LAST_DRAWING_DELETE("-012", HttpStatus.BAD_REQUEST, "거래에는 최소 하나의 도면이 필요합니다.마지막 도면은 삭제할 수 없습니다."),
    MISSING_QUOTATION_FILE("-013", HttpStatus.BAD_REQUEST, "견적서 최초 작성 시, 견적서 파일은 필수 사항입니다."),
    MISSING_QUOTATION("-014", HttpStatus.BAD_REQUEST, "거래의 견적서가 존재하지 않습니다."),
    MISSING_PURCHASE_ORDER("-015", HttpStatus.BAD_REQUEST, "거래의 발주서가 존재하지 않습니다."),
    DEFAULT_DELIVERY_ADDRESS_DELETE("-016", HttpStatus.BAD_REQUEST, "기본 배송지는 삭제할 수 없습니다."),
    MISSING_PURCHASE_ORDER_FILE("-017", HttpStatus.BAD_REQUEST, "발주서 최초 작성 시, 발주서 파일은 필수 사항입니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    UNAUTHORIZED("-100", HttpStatus.UNAUTHORIZED, "인증 자격 정보가 유효하지 않습니다."),
    INVALID_JWT_TOKEN("-101", HttpStatus.UNAUTHORIZED, "JWT Token 정보가 유효하지 않습니다."),
    EXPIRED_JWT_TOKEN("-102", HttpStatus.UNAUTHORIZED, "JWT 토큰 정보가 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN("-103", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰 입니다."),
    UNAUTHORIZED_JWT_TOKEN("-104", HttpStatus.UNAUTHORIZED, "JWT 토큰에 권한정보가 존재하지 않습니다."),

    INVALID_ACCESS_JWT_TOKEN("-105", HttpStatus.UNAUTHORIZED, "Access JWT 토큰 정보가 유효하지 않습니다."),
    INVALID_REFRESH_JWT_TOKEN("-106", HttpStatus.UNAUTHORIZED, "Refresh JWT 토큰 정보가 유효하지 않습니다."),

    INVALID_VERIFY_CODE("-107", HttpStatus.UNAUTHORIZED, "인증 코드 정보가 유효하지 않습니다."),

    INVALID_CHANGE_PASSWORD_JWT_TOKEN("-108", HttpStatus.UNAUTHORIZED, "Change Password JWT 토큰 정보가 유효하지 않습니다."),

    // 403 FORBIDDEN 인증 필요
    FORBIDDEN("-300", HttpStatus.FORBIDDEN, "인증이 필요합니다."),
    DENIED_ACCESS("-301", HttpStatus.FORBIDDEN, "해당 요청에 대한 접근 권한이 없습니다."),
    DENIED_ACCESS_TO_ENTITY("-302", HttpStatus.FORBIDDEN, " 엔티티에 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND("-400", HttpStatus.NOT_FOUND, "리소스가 존재하지 않습니다."),
    NOT_FOUND_VERIFY_CODE("-401", HttpStatus.NOT_FOUND, "이메일에 해당하는 인증 코드가 존재하지 않습니다."),
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
