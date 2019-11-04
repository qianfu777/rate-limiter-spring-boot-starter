package com.qianfu.rate.limiter.annotations;

import com.qianfu.rate.limiter.enums.Realize;
import com.qianfu.rate.limiter.enums.Type;

import java.lang.annotation.*;

/**
 * The interface Limiter.
 *
 * @author qianfu
 * @date 2019 /11/1
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {
    /**
     * 限流器的类型，默认是分布式
     *
     * @see Type
     */
    Type type() default Type.DISTRIBUTE;

    /**
     * 限流器的实现，默认是滑动窗口
     *
     * @see Realize
     */
    Realize realize() default Realize.WINDOW;

    /**
     * 限流器的key，相同key共享一个限流器
     */
    String key() default "default";

    /**
     * 每秒限流数
     */
    int limit() default 100;
}
