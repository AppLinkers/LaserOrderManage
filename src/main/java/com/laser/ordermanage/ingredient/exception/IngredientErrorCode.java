package com.laser.ordermanage.ingredient.exception;

import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum IngredientErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    UNABLE_UPDATE_DELETED_INGREDIENT("INGREDIENT_400_01", HttpStatus.BAD_REQUEST, "삭제된 자재는 수정할 수 없습니다."),
    UNABLE_DELETE_DELETED_INGREDIENT("INGREDIENT_400_02", HttpStatus.BAD_REQUEST, "이미 삭제된 자재는 삭제할 수 없습니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS_TO_INGREDIENT("INGREDIENT_403_01", HttpStatus.FORBIDDEN, "자재 대한 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_INGREDIENT("INGREDIENT_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 자재 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
