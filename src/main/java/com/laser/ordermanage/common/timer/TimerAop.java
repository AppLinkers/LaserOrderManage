package com.laser.ordermanage.common.timer;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class TimerAop {

    @Pointcut("@annotation(com.laser.ordermanage.common.timer.Timer)")
    private void enableTimer(){}

    @Around("enableTimer()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        joinPoint.proceed();

        stopWatch.stop();
        log.info(joinPoint.getSignature().getName() + " - total time : " + stopWatch.getTotalTimeSeconds() + "초");
    }
}
