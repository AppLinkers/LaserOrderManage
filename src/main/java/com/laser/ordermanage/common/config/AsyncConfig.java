package com.laser.ordermanage.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    @Bean(name = "asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 최소 스레드 풀 사이즈
        executor.setCorePoolSize(10);
        // 최대 스레드 풀 사이즈
        executor.setMaxPoolSize(100);
        // 대기열 길이
        executor.setQueueCapacity(1000);
        // 스레드 프리픽스
        executor.setThreadNamePrefix("asyncExecutor-");
        // ThreadPoolExecutor 구성
        executor.initialize();
        return executor;
    }

    // 비동기 예외 처리
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (exceptionHandler, method, params) ->
                log.error("Exception handler for async method '" + method.toGenericString()
                        + "' threw unexpected exception itself", exceptionHandler);
    }
}
