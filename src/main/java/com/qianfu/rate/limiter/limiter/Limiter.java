package com.qianfu.rate.limiter.limiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qianfu
 * @date 2019/11/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Limiter {
    protected static final int ONE_SECOND_MILLS = 1000;
    protected String key;
    protected int limit;

    public abstract boolean tryAcquire();
}
