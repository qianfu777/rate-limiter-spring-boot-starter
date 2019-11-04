package com.qianfu.rate.limiter.limiter.local;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于漏桶实现的本地限流器
 *
 * @author qianfu
 * @date 2019/11/1
 */
public class LocalLeakyBucketLimiter extends AbstractLocalLimiter {

    private int rate;
    private int capacity;
    private AtomicInteger water;
    private AtomicLong refreshTime;

    public LocalLeakyBucketLimiter(String key, int limit) {
        super(key, limit);
        rate = 1;
        capacity = limit;
        water = new AtomicInteger(0);
        refreshTime = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    public boolean tryAcquire() {
        refreshWater();
        if (water.get() < capacity) {
            water.incrementAndGet();
            return true;
        }

        return false;
    }

    private void refreshWater() {
        int tempWatch = water.get();
        long tempTime = refreshTime.get();
        long now = System.currentTimeMillis();
        //水随着时间流逝,不断流走,最多就流干到0.
        water.compareAndSet(tempWatch, (int) Math.max(0, tempWatch - (now - tempTime) / 1000 * rate));
        refreshTime.compareAndSet(tempTime, now);
    }
}
