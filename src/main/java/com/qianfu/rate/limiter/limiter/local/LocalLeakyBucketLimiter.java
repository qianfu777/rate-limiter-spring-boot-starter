package com.qianfu.rate.limiter.limiter.local;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 基于漏桶实现的本地限流器
 *
 * @author qianfu
 * @date 2019/11/1
 */
public class LocalLeakyBucketLimiter extends AbstractLocalLimiter {


    public LocalLeakyBucketLimiter(String key, int limit) {
        super(key, limit);
        RateLimiter rateLimiter = RateLimiter.create(1);
    }

    @Override
    public boolean tryAcquire() {
        return false;
    }
}
