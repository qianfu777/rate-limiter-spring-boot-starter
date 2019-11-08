package com.qianfu.rate.limiter.limiter.local;

/**
 * 基于漏桶实现的本地限流器
 *
 * @author qianfu
 * @date 2019/11/1
 */
public class LocalLeakyBucketLimiter extends AbstractLocalLimiter {

    private int rate;
    private int capacity;
    private int water;
    private long refreshTime;

    public LocalLeakyBucketLimiter(String key, int limit) {
        super(key, limit);
        rate = 1;
        capacity = limit;
        water = 0;
        refreshTime = System.currentTimeMillis();
    }

    @Override
    public boolean tryAcquire() {
        synchronized (lock) {
            refreshWater();
            if (water < capacity) {
                water++;
                return true;
            }
            return false;
        }
    }

    private void refreshWater() {
        long now = System.currentTimeMillis();
        // 水随着时间流逝,不断流走,最多就流干到0.
        water = (int) Math.max(0, water - (now - refreshTime) / 1000 * rate);
        refreshTime = now;
    }
}
