package com.laser.ordermanage.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.security.config.TestWebSecurityConfig;
import com.laser.ordermanage.common.security.jwt.component.JwtAccessDeniedHandler;
import com.laser.ordermanage.common.security.jwt.component.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API Unit Test
 * 1. 테스트 성공
 * 2. Role, Authority 검증
 * 3. 요청 데이터(필드 및 파라미터) 검증
 * 4. 해당 API 의 서비스 코드에서 발생하는 예외 검증
 */
@ActiveProfiles("test")
@ContextConfiguration(classes = TestWebSecurityConfig.class)
@Import({JwtAccessDeniedHandler.class, JwtAuthenticationEntryPoint.class})
public class APIUnitTest {

    protected MockMvc mvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    public MockMvc buildMockMvc(WebApplicationContext context) {
        return MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    public void assertError(ErrorCode expected, ResultActions actual) throws Exception {
        actual
                .andExpect(status().is(expected.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(expected.getCode()))
                .andExpect(jsonPath("message").value(expected.getMessage()));
    }

    public void assertErrorWithMessage(ErrorCode expected, ResultActions actual, String message) throws Exception {
        actual
                .andExpect(status().is(expected.getHttpStatus().value()))
                .andExpect(jsonPath("errorCode").value(expected.getCode()))
                .andExpect(jsonPath("message").value(message + expected.getMessage()));
    }
}
