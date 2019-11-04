package com.qianfu.rate.limiter.limiter.local;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于计数器实现的本地限流器
 *
 * @author qianfu
 * @date 2019/11/1
 */
public class LocalCountLimiter extends AbstractLocalLimiter {

    private AtomicLong refreshTime;
    private AtomicInteger count;

    public LocalCountLimiter(String key, int limit) {
        super(key, limit);
        count = new AtomicInteger(0);
        refreshTime = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    public boolean tryAcquire() {
        int tempCount = count.get();
        long tempTime = refreshTime.get();
        long now = System.currentTimeMillis();
        if (now - tempTime >= ONE_SECOND_MILLS) {
            count.compareAndSet(tempCount, 0);
            refreshTime.compareAndSet(tempTime, now);
        }
        return count.incrementAndGet() <= limit;
    }
}
