package com.laser.ordermanage.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomCommonException.class)
    public ResponseEntity<?> handleCustomCommonException(CustomCommonException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.toErrorResponse());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialException(BadCredentialsException e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.INVALID_CREDENTIALS);
        return ResponseEntity.badRequest().body(exception.toErrorResponse());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(Authentication e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.DENIED_ACCESS);
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorResponse());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> objectErrorList = e.getBindingResult().getAllErrors();

        StringBuilder sb = new StringBuilder();
        objectErrorList.forEach(
                objectError -> sb.append(objectError.getDefaultMessage())
        );

        CustomCommonException exception = new CustomCommonException(ErrorCode.INVALID_FIELDS, sb.toString());

        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorResponse());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(
                constraintViolation -> sb.append(constraintViolation.getMessage())
        );

        CustomCommonException exception = new CustomCommonException(ErrorCode.INVALID_FIELDS, sb.toString());

        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorResponse());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.INVALID_PARAMETER_TYPE, e.getName());
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorResponse());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<?> handleMissingRequestCookieException(MissingRequestCookieException e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.MISSING_COOKIE, e.getCookieName());
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorResponse());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingRequestParameterException(MissingServletRequestParameterException e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.MISSING_QUERY_PARAMETER, e.getParameterName());
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorResponse());
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<?> handleSizeLimitExceededException(SizeLimitExceededException e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.REQUEST_FILE_SIZE_EXCEED);
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorResponse());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        CustomCommonException exception = new CustomCommonException(ErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toErrorResponse());
    }
}
