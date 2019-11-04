package com.qianfu.rate.limiter.limiter.local;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 基于令牌桶实现的本地限流器
 *
 * @author qianfu
 * @date 2019/11/4
 */
public class LocalTokenBucketLimiter extends AbstractLocalLimiter {

    private RateLimiter rateLimiter;

    public LocalTokenBucketLimiter(String key, int limit) {
        super(key, limit);
        rateLimiter = RateLimiter.create(1);
    }

    @Override
    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }
}
