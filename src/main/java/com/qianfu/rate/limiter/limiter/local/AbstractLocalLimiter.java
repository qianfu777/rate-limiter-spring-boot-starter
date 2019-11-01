package com.qianfu.rate.limiter.limiter.local;

import com.qianfu.rate.limiter.limiter.Limiter;

/**
 * @author qianfu
 * @date 2019/11/1
 */
public abstract class AbstractLocalLimiter extends Limiter {
    protected final String lock;

    public AbstractLocalLimiter(String key, int limit) {
        super(key, limit);
        lock = key;
    }
}
