package com.qianfu.rate.limiter.config;

import com.qianfu.rate.limiter.annotations.RateLimiter;
import com.qianfu.rate.limiter.service.LimiterHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author qianfu
 * @date 2019/11/1
 */
@Slf4j
@Aspect
@Configuration
public class LimiterAspectConfiguration {

    @Resource
    private LimiterHandler limiterHandler;

    @Pointcut("@annotation(com.qianfu.rate.limiter.annotations.RateLimiter)")
    private void point() {
    }

    @Around("point()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RateLimiter annotation = method.getAnnotation(RateLimiter.class);

        if (!limiterHandler.tryAcquire(annotation)) {
            String info = "[" + annotation.key() + "] has over limit";
            log.info(info);
            throw new RuntimeException(info);
        }

        try {
            return pjp.proceed();
        } catch (Exception e) {
            log.error("execute rate limiter method [" + annotation.key() + "] throw an exception", e);
        }
        return null;
    }

}
