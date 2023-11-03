package com.laser.ordermanage.common.security.jwt.component;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String)request.getAttribute("exception");

        if(exception == null) {
            resolver.resolveException(request, response, null, new CustomCommonException(ErrorCode.UNKNOWN_ERROR));
        }
        // header 에 토큰이 없는 경우
        else if(exception.equals(ErrorCode.MISSING_JWT_TOKEN.getCode())) {
            resolver.resolveException(request, response, null, new CustomCommonException(ErrorCode.MISSING_JWT_TOKEN));
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(ErrorCode.INVALID_JWT_TOKEN.getCode())) {
            resolver.resolveException(request, response, null, new CustomCommonException(ErrorCode.INVALID_JWT_TOKEN));
        }
        //토큰 만료된 경우
        else if(exception.equals(ErrorCode.EXPIRED_JWT_TOKEN.getCode())) {
            resolver.resolveException(request, response, null, new CustomCommonException(ErrorCode.EXPIRED_JWT_TOKEN));
        }
        //지원되지 않는 토큰인 경우
        else if(exception.equals(ErrorCode.UNSUPPORTED_JWT_TOKEN.getCode())) {
            resolver.resolveException(request, response, null, new CustomCommonException(ErrorCode.UNSUPPORTED_JWT_TOKEN));
        }
        else {
            resolver.resolveException(request, response, null, new CustomCommonException(ErrorCode.DENIED_ACCESS));
        }

    }
}
