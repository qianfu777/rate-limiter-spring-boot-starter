package com.qianfu.rate.limiter.enums;

/**
 * @author qianfu
 * @date 2019/11/1
 */
public enum Realize {
    /**
     * 计数器
     */
    COUNT,
    /**
     * 滑动窗口
     */
    WINDOW,
    /**
     * 漏桶
     */
    LEAKY_BUCKET,
    /**
     * 令牌桶
     */
    TOKEN_BUCKET,
    ;
}
