package com.qianfu.rate.limiter.service;

import com.qianfu.rate.limiter.annotations.RateLimiter;
import com.qianfu.rate.limiter.enums.Realize;
import com.qianfu.rate.limiter.enums.Type;
import com.qianfu.rate.limiter.limiter.Limiter;
import com.qianfu.rate.limiter.limiter.distributed.DistributedCountLimiter;
import com.qianfu.rate.limiter.limiter.distributed.DistributedWindowLimiter;
import com.qianfu.rate.limiter.limiter.local.LocalCountLimiter;
import com.qianfu.rate.limiter.limiter.local.LocalLeakyBucketLimiter;
import com.qianfu.rate.limiter.limiter.local.LocalTokenBucketLimiter;
import com.qianfu.rate.limiter.limiter.local.LocalWindowLimiter;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qianfu
 * @date 2019/11/1
 */
public class LimiterHandler {
    private static final String LOCK = "lock";
    private StringRedisTemplate redisTemplate;

    public LimiterHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Limiter create(String key, int limit) {
        return create(Type.DISTRIBUTE, Realize.WINDOW, key, limit);
    }

    public Limiter create(Type type, Realize realize, String key, int limit) {
        if (Type.LOCAL.equals(type)) {
            switch (realize) {
                case COUNT:
                    return new LocalCountLimiter(key, limit);
                case WINDOW:
                    return new LocalWindowLimiter(key, limit);
                case LEAKY_BUCKET:
                    return new LocalLeakyBucketLimiter(key, limit);
                default:
                    return new LocalTokenBucketLimiter(key, limit);
            }
        }

        if (Type.DISTRIBUTE.equals(type)) {
            switch (realize) {
                case COUNT:
                    return new DistributedCountLimiter(key, limit, redisTemplate);
                case WINDOW:
                    return new DistributedWindowLimiter(key, limit, redisTemplate);
                case LEAKY_BUCKET:
                    return new LocalLeakyBucketLimiter(key, limit);
                default:
                    return new LocalTokenBucketLimiter(key, limit);
            }
        }

        return null;
    }

    private Map<String, Limiter> limiterMap = new ConcurrentHashMap<>();

    public boolean tryAcquire(RateLimiter annotation) {
        Limiter limiter = limiterMap.get(annotation.key());
        if (limiter == null) {
            synchronized (LOCK) {
                limiter = limiterMap.get(annotation.key());
                if (limiter == null) {
                    limiter = create(annotation.type(), annotation.realize(), annotation.key(), annotation.limit());
                    limiterMap.put(annotation.key(), limiter);
                }
            }
        }

        return limiter.tryAcquire();
    }
}
