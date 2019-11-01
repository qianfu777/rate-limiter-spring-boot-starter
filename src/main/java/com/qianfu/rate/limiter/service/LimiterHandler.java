package com.qianfu.rate.limiter.service;

import com.qianfu.rate.limiter.enums.Realize;
import com.qianfu.rate.limiter.enums.Type;
import com.qianfu.rate.limiter.limiter.Limiter;
import com.qianfu.rate.limiter.limiter.local.LocalCountLimiter;
import com.qianfu.rate.limiter.limiter.local.LocalWindowLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qianfu
 * @date 2019/11/1
 */
public class LimiterHandler {
    public static Limiter create(String key, int limit) {
        return create(Type.DISTRIBUTE, Realize.WINDOW, key, limit);
    }

    public static Limiter create(Type type, Realize realize, String key, int limit) {
        if (Type.LOCAL.equals(type)) {
            switch (realize) {
                case COUNT:
                    return new LocalCountLimiter(key, limit);
                case WINDOW:
                    return new LocalWindowLimiter(key, limit);
                case LEAKY_BUCKET:
                    return null;
                case TOKEN_BUCKET:
                    return null;
                default:
                    return new LocalWindowLimiter(key, limit);
            }
        }

        if (Type.DISTRIBUTE.equals(type)) {

        }

        return null;
    }

    private Map<String, Limiter> limiterMap = new ConcurrentHashMap<>();


}
