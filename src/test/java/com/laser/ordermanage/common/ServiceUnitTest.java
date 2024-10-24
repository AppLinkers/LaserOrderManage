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
 * Service 컴포넌트의 역할
 * - 도메인 객체를 저장소에서 불러옵니다.
 * - 도메인 객체나 도메인 서비스에 일을 시킵니다. (메서드 호출)
 * - 도메인 객체의 변경사항을 저장소에 저장합니다.
 * - 컨트롤러에서 요구한 내용을 응답한다.
 */

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
