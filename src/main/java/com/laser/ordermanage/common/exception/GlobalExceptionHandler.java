package com.laser.ordermanage.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomCommonException.class)
    public ResponseEntity<?> customCommonException(CustomCommonException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.toErrorRes());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialException(BadCredentialsException e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.INVALID_CREDENTIALS);
        return ResponseEntity.badRequest().body(exception.toErrorRes());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(Authentication e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.DENIED_ACCESS);
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorRes());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> objectErrorList = e.getBindingResult().getAllErrors();

        StringBuilder sb = new StringBuilder();
        objectErrorList.forEach(
                objectError -> sb.append(objectError.getDefaultMessage())
        );

        CustomCommonException exception = new CustomCommonException(ErrorCode.INVALID_FIELDS, sb.toString());

        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorRes());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.INVALID_PARAMETER_TYPE, e.getName());
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorRes());
    }
}
