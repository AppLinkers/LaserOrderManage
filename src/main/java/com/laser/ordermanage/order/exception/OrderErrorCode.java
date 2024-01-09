package com.laser.ordermanage.order.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum OrderErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    NOT_SUPPORTED_DRAWING_FILE_EXTENSION("ORDER_400_01", HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    NOT_SUPPORTED_INGREDIENT("ORDER_400_02", HttpStatus.BAD_REQUEST, "지원하지 않는 재료입니다."),
    INVALID_ORDER_STAGE("ORDER_400_03", HttpStatus.BAD_REQUEST, " 단계의 거래는 해당 요청을 수행할 수 없습니다."),
    LAST_DRAWING_DELETE("ORDER_400_04", HttpStatus.BAD_REQUEST, "거래에는 최소 하나의 도면이 필요합니다.마지막 도면은 삭제할 수 없습니다."),
    REQUIRED_QUOTATION_FILE("ORDER_400_05", HttpStatus.BAD_REQUEST, "견적서 최초 작성 시, 견적서 파일은 필수 사항입니다."),
    MISSING_QUOTATION("ORDER_400_06", HttpStatus.BAD_REQUEST, "거래의 견적서가 존재하지 않습니다."),
    MISSING_PURCHASE_ORDER("ORDER_400_07", HttpStatus.BAD_REQUEST, "거래의 발주서가 존재하지 않습니다."),
    REQUIRED_PURCHASE_ORDER_FILE("ORDER_400_08", HttpStatus.BAD_REQUEST, "발주서 최초 작성 시, 발주서 파일은 필수 사항입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
