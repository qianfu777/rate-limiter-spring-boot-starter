package com.qianfu.rate.limiter.limiter.distributed;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于滑动窗口实现的分布式限流器
 *
 * @author qianfu
 * @date 2019/11/4
 */
public class DistributedWindowLimiter extends AbstractDistributedLimiter {
    private DefaultRedisScript<Long> script;

    public DistributedWindowLimiter(String key, int limit, StringRedisTemplate redisTemplate) {
        super(key, limit, redisTemplate);
        script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/window.lua")));
    }

    @Override
    public boolean tryAcquire() {
        long now = System.currentTimeMillis() % 100;
        List<String> keys = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            keys.add(redisKey + "-" + (now - i));
        }
        return redisTemplate.execute(script, keys) <= limit;
    }
}
