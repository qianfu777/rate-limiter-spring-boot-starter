package com.qianfu.rate.limiter.limiter.distributed;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 基于计数器实现的分布式限流器
 *
 * @author qianfu
 * @date 2019/11/4
 */
public class DistributedCountLimiter extends AbstractDistributedLimiter {

    public DistributedCountLimiter(String key, int limit, StringRedisTemplate redisTemplate) {
        super(key, limit, redisTemplate);
    }

    @Override
    public boolean tryAcquire() {
        String countKey = redisKey + "-" + System.currentTimeMillis() / 1000;
        int count = redisTemplate.opsForValue().increment(countKey, 1).intValue();
        redisTemplate.expire(countKey, 2, TimeUnit.SECONDS);
        return count <= limit;
    }
}
