package com.laser.ordermanage.common.exception.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class InvalidFieldsRes {

    HttpStatus httpStatus;
    List<String> errorMessageList;
}
