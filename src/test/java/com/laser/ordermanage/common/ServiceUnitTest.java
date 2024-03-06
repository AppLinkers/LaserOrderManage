package com.laser.ordermanage.common;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

/**
 * Service Unit Test
 * 1. 테스트 성공 (비즈니스 로직 검증)
 * 2. 서비스 코드에서 발생하는 예외 검증
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ServiceUnitTest {
}
