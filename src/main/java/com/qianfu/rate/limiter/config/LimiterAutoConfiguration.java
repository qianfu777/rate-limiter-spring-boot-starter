package com.qianfu.rate.limiter.config;

import com.qianfu.rate.limiter.service.LimiterHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author qianfu
 * @date 2019/11/1
 */
@Configuration
@ConditionalOnClass({LimiterHandler.class})
public class LimiterAutoConfiguration {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    @ConditionalOnMissingBean(LimiterHandler.class)
    public LimiterHandler limiterHandler() {
        return new LimiterHandler(stringRedisTemplate);
    }
}
