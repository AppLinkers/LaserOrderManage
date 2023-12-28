package com.laser.ordermanage.common.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laser.ordermanage.common.exception.CustomCommonException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (CustomCommonException e) {
            createJwtErrorResponse(request, response, e);
        }
    }

    private void createJwtErrorResponse(HttpServletRequest request, HttpServletResponse response, CustomCommonException e) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        String responseBody = objectMapper.writeValueAsString(e.toErrorResponse());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(e.getHttpStatus().value());
        response.getWriter().write(responseBody);
    }
}
