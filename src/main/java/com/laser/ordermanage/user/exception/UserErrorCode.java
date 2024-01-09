package com.laser.ordermanage.user.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    MISSING_JWT("USER_400_01", HttpStatus.BAD_REQUEST, "JWT 정보가 요청에 포함되지 않았습니다."),
    INVALID_CREDENTIALS("USER_400_02", HttpStatus.BAD_REQUEST, "ID 또는 비밀번호가 올바르지 않습니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    INVALID_JWT("USER_401_01", HttpStatus.UNAUTHORIZED, "JWT 정보가 유효하지 않습니다."),
    EXPIRED_JWT("USER_401_02", HttpStatus.UNAUTHORIZED, "JWT 정보가 만료되었습니다."),
    UNSUPPORTED_JWT("USER_401_03", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 입니다."),
    UNAUTHORIZED_JWT("USER_401_04", HttpStatus.UNAUTHORIZED, "JWT 에 권한정보가 존재하지 않습니다."),

    INVALID_ACCESS_TOKEN("USER_401_05", HttpStatus.UNAUTHORIZED, "Access Token 정보가 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN("USER_401_06", HttpStatus.UNAUTHORIZED, "Refresh Token 정보가 유효하지 않습니다."),

    INVALID_VERIFY_CODE("USER_401_07", HttpStatus.UNAUTHORIZED, "인증 코드 정보가 유효하지 않습니다."),

    INVALID_CHANGE_PASSWORD_TOKEN("USER_401_08", HttpStatus.UNAUTHORIZED, "Change Password Token 정보가 유효하지 않습니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS("USER_403_01", HttpStatus.FORBIDDEN, "해당 요청에 대한 접근 권한이 없습니다."),
    DENIED_ACCESS_TO_ENTITY("USER_403_02", HttpStatus.FORBIDDEN, " 엔티티에 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_VERIFY_CODE("USER_404_01", HttpStatus.NOT_FOUND, "이메일에 해당하는 인증 코드가 존재하지 않습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
