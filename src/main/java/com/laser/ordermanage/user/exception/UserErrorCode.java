package com.laser.ordermanage.user.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    INVALID_CREDENTIALS("-002", HttpStatus.BAD_REQUEST, "ID 또는 비밀번호가 올바르지 않습니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    INVALID_JWT_TOKEN("-101", HttpStatus.UNAUTHORIZED, "JWT Token 정보가 유효하지 않습니다."),
    EXPIRED_JWT_TOKEN("-102", HttpStatus.UNAUTHORIZED, "JWT 토큰 정보가 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN("-103", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰 입니다."),
    UNAUTHORIZED_JWT_TOKEN("-104", HttpStatus.UNAUTHORIZED, "JWT 토큰에 권한정보가 존재하지 않습니다."),

    INVALID_ACCESS_JWT_TOKEN("-105", HttpStatus.UNAUTHORIZED, "Access JWT 토큰 정보가 유효하지 않습니다."),
    INVALID_REFRESH_JWT_TOKEN("-106", HttpStatus.UNAUTHORIZED, "Refresh JWT 토큰 정보가 유효하지 않습니다."),

    INVALID_VERIFY_CODE("-107", HttpStatus.UNAUTHORIZED, "인증 코드 정보가 유효하지 않습니다."),

    INVALID_CHANGE_PASSWORD_JWT_TOKEN("-108", HttpStatus.UNAUTHORIZED, "Change Password JWT 토큰 정보가 유효하지 않습니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS("-301", HttpStatus.FORBIDDEN, "해당 요청에 대한 접근 권한이 없습니다."),
    DENIED_ACCESS_TO_ENTITY("-302", HttpStatus.FORBIDDEN, " 엔티티에 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_VERIFY_CODE("-401", HttpStatus.NOT_FOUND, "이메일에 해당하는 인증 코드가 존재하지 않습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
