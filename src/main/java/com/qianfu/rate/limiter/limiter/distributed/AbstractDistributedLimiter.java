package com.qianfu.rate.limiter.limiter.distributed;

import com.qianfu.rate.limiter.limiter.Limiter;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 分布式限流器
 *
 * @author qianfu
 * @date 2019/11/1
 */
public abstract class AbstractDistributedLimiter extends Limiter {

    protected StringRedisTemplate redisTemplate;

    public AbstractDistributedLimiter(String key, int limit, StringRedisTemplate redisTemplate) {
        super(key, limit);
        this.redisTemplate = redisTemplate;
    }
}
