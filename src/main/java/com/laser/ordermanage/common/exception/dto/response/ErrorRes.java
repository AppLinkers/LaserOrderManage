package com.laser.ordermanage.common.exception.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
public class ErrorRes {

    private HttpStatus httpStatus;
    private String message;

}
