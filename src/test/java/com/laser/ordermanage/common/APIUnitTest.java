package com.laser.ordermanage.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laser.ordermanage.common.security.config.TestWebSecurityConfig;
import com.laser.ordermanage.common.security.jwt.component.JwtAccessDeniedHandler;
import com.laser.ordermanage.common.security.jwt.component.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

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

}
