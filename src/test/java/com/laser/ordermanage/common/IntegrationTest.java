package com.laser.ordermanage.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laser.ordermanage.OrderManageApplication;
import com.laser.ordermanage.common.cache.redis.config.RedisTestContainers;
import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.SneakyThrows;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrderManageApplication.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(RedisTestContainers.class)
@Transactional
public class IntegrationTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected SchedulerFactoryBean schedulerFactoryBean;

    @SneakyThrows
    @After
    public void 스케줄_초기화() {
        schedulerFactoryBean.getScheduler().clear();
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
