package com.laser.ordermanage.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

/**
 * Service Unit Test
 * 1. 테스트 성공 (비즈니스 로직 검증)
 * 2. 서비스 코드에서 발생하는 예외 검증
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ServiceUnitTest {

    protected ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void setUpForAsync(Executor asyncExecutor) {
        // Async -> Sync Test
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(asyncExecutor).execute(any(Runnable.class));
    }
}
