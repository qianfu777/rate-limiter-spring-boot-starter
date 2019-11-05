package com.qianfu.rate.limiter.limiter.distributed;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;

/**
 * 基于漏桶实现的分布式限流器
 *
 * @author qianfu
 * @date 2019/11/5
 */
public class DistributeLeakyBucketLimiter extends AbstractDistributedLimiter {
    private DefaultRedisScript<Long> script;


    public DistributeLeakyBucketLimiter(String key, int limit, StringRedisTemplate redisTemplate) {
        super(key, limit, redisTemplate);
        script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/leaky_bucket.lua")));
    }

    @Override
    public boolean tryAcquire() {
        return redisTemplate.execute(script, Collections.singletonList(key)) <= limit;
    }
}
