package com.laser.ordermanage.common.exception;

import com.laser.ordermanage.common.slack.SlackService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackService slackService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(Exception e, HttpServletRequest request) {
        slackService.sendSlackAlertErrorLog(e, request); // 슬랙 알림 보내는 메서드
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.UNKNOWN_ERROR);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(CustomCommonException.class)
    public ResponseEntity<?> handleCustomCommonException(CustomCommonException e) {
        return e.toErrorResponse();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(InsufficientAuthenticationException e) {
        CustomCommonException exception = new CustomCommonException(UserErrorCode.MISSING_JWT);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialException(BadCredentialsException e) {
        CustomCommonException exception = new CustomCommonException(UserErrorCode.INVALID_CREDENTIALS);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(Authentication e) {
        CustomCommonException exception = new CustomCommonException(UserErrorCode.DENIED_ACCESS);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.MISMATCH_PARAMETER_TYPE, e.getName());
        return exception.toErrorResponse();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingRequestParameterException(MissingServletRequestParameterException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.REQUIRED_PARAMETER, e.getParameterName());
        return exception.toErrorResponse();
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.REQUIRED_PARAMETER, e.getRequestPartName());
        return exception.toErrorResponse();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(
                constraintViolation -> sb.append(constraintViolation.getMessage())
        );

        CustomCommonException exception = new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, sb.toString());

        return exception.toErrorResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> objectErrorList = e.getBindingResult().getAllErrors();

        StringBuilder sb = new StringBuilder();
        objectErrorList.forEach(
                objectError -> sb.append(objectError.getDefaultMessage())
        );

        CustomCommonException exception = new CustomCommonException(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, sb.toString());

        return exception.toErrorResponse();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.REQUIRED_REQUEST_BODY);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<?> handleMissingRequestCookieException(MissingRequestCookieException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.REQUIRED_COOKIE, e.getCookieName());
        return exception.toErrorResponse();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.REQUEST_SIZE_EXCEEDED);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<?> handleSizeLimitExceededException(SizeLimitExceededException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.REQUEST_SIZE_EXCEEDED);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<?> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.REQUEST_FILE_SIZE_EXCEEDED);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.METHOD_NOT_ALLOWED);
        return exception.toErrorResponse();
    }

}
