package com.qianfu.rate.limiter.limiter.local;

/**
 * 基于计数器实现的本地限流器
 *
 * @author qianfu
 * @date 2019/11/1
 */
public class LocalCountLimiter extends AbstractLocalLimiter {

    private long timestamp;
    private int count;

    public LocalCountLimiter(String key, int limit) {
        super(key, limit);
        count = 0;
        timestamp = System.currentTimeMillis();
    }

    @Override
    public boolean tryAcquire() {
        synchronized (lock) {
            long now = System.currentTimeMillis();
            if (now - timestamp >= ONE_SECOND_MILLS) {
                count = 0;
                timestamp = now;
            }
            return ++count < limit;
        }
    }
}
