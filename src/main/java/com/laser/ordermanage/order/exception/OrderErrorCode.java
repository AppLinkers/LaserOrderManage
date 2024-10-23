package com.laser.ordermanage.order.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum OrderErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    UNSUPPORTED_DRAWING_FILE_EXTENSION("ORDER_400_01", HttpStatus.BAD_REQUEST, "지원하지 않는 도면 파일 형식입니다."),
    UNSUPPORTED_INGREDIENT("ORDER_400_02", HttpStatus.BAD_REQUEST, "지원하지 않는 재료입니다."),
    INVALID_ORDER_STAGE("ORDER_400_03", HttpStatus.BAD_REQUEST, "%s 단계의 거래는 해당 요청을 수행할 수 없습니다."),
    LAST_DRAWING_DELETE("ORDER_400_04", HttpStatus.BAD_REQUEST, "거래에는 최소 하나의 도면이 필요합니다.마지막 도면은 삭제할 수 없습니다."),
    UNSUPPORTED_QUOTATION_FILE_EXTENSION("ORDER_400_05", HttpStatus.BAD_REQUEST, "지원하지 않는 견적서 파일 형식입니다."),
    REQUIRED_QUOTATION_FILE("ORDER_400_06", HttpStatus.BAD_REQUEST, "견적서 최초 작성 시, 견적서 파일은 필수 사항입니다."),
    UNSUPPORTED_PURCHASE_ORDER_FILE_EXTENSION("ORDER_400_07", HttpStatus.BAD_REQUEST, "지원하지 않는 발주서 파일 형식입니다."),
    REQUIRED_PURCHASE_ORDER_FILE("ORDER_400_08", HttpStatus.BAD_REQUEST, "발주서 최초 작성 시, 발주서 파일은 필수 사항입니다."),
    UNSUPPORTED_SIGNATURE_FILE_EXTENSION("ORDER_400_09", HttpStatus.BAD_REQUEST, "지원하지 않는 서명 파일 형식입니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS_TO_ORDER("ORDER_403_01", HttpStatus.FORBIDDEN, "거래에 대한 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_ORDER("ORDER_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 거래 입니다."),
    NOT_FOUND_DRAWING("ORDER_404_02", HttpStatus.NOT_FOUND, "존재하지 않는 도면 입니다."),
    NOT_FOUND_QUOTATION("ORDER_404_03", HttpStatus.NOT_FOUND, "거래의 견적서가 존재하지 않습니다."),
    NOT_FOUND_PURCHASE_ORDER("ORDER_404_04", HttpStatus.NOT_FOUND, "거래의 발주서가 존재하지 않습니다."),
    NOT_FOUND_COMMENT("ORDER_404_05", HttpStatus.NOT_FOUND, "존재하지 않는 댓글 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
