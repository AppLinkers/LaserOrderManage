package com.laser.ordermanage.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laser.ordermanage.OrderManageApplication;
import com.laser.ordermanage.common.cache.redis.config.RedisTestContainers;
import lombok.SneakyThrows;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

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
}
