package com.laser.ordermanage.common.exception;

import com.laser.ordermanage.common.util.NetworkUtil;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
//        sendSlackAlertErrorLog(e, request); // 슬랙 알림 보내는 메서드
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
    protected ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        CustomCommonException exception = new CustomCommonException(CommonErrorCode.METHOD_NOT_ALLOWED);
        return exception.toErrorResponse();
    }

    // 슬랙 알림 보내는 메서드
    private void sendSlackAlertErrorLog(Exception e, HttpServletRequest request) {
        try {
            slackClient.send(webhookUrl, payload(p -> p
                    .text("서버 에러 발생!")
                    .attachments(
                            List.of(generateSlackAttachment(e, request))
                    )
            ));
        } catch (IOException slackError) {
            // slack 통신 시 발생한 예외에서 Exception을 던져준다면 재귀적인 예외가 발생합니다.
            // 따라서 로깅으로 처리하였고, 서버 에러는 아니므로 `error` 레벨보다 낮은 레벨로 설정했습니다.
            log.debug("Slack 통신과의 예외 발생");
        }
    }

    // attachment 생성 메서드
    private Attachment generateSlackAttachment(Exception e, HttpServletRequest request) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
        String requestIP = NetworkUtil.getClientIp(request);

        List<Field> fieldList = new ArrayList<>();
        fieldList.add(new Field("Request IP", requestIP, false));
        fieldList.add(new Field("Request URL", request.getMethod() + " " + request.getRequestURL(), false));

        if (request.getContentType() != null && request.getContentType().contains("application/json")) {
            try {
                String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
                fieldList.add(new Field("Request Body", messageBody, false));
            } catch (IOException ex) {
                log.debug("Extract Request Body 예외 발생");
            }
        }

        fieldList.add(new Field("Error Message", e.getMessage(), false));

        return Attachment.builder()
                .color("ff0000")  // 붉은 색으로 보이도록
                .title(requestTime + " 발생한 에러 로그")
                .fields(fieldList)
                .build();
    }

}
