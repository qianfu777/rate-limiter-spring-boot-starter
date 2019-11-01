package com.qianfu.rate.limiter.limiter.local;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于滑动窗口实现的本地限流器
 *
 * @author qianfu
 * @date 2019/11/1
 */
public class LocalWindowLimiter extends AbstractLocalLimiter {

    private Map<Long, Integer> countMap;

    public LocalWindowLimiter(String key, int limit) {
        super(key, limit);
        countMap = new LinkedHashMap<Long, Integer>(10) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > 10;
            }
        };
    }

    @Override
    public boolean tryAcquire() {
        synchronized (lock) {
            long now = System.currentTimeMillis() / 100;
            countMap.merge(now, 0, (time, count) -> count++);

            int totalCount = 0;
            for (Iterator<Map.Entry<Long, Integer>> it = countMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Long, Integer> entry = countMap.entrySet().iterator().next();
                if (now - entry.getKey() > 10) {
                    it.remove();
                } else {
                    totalCount += entry.getValue();
                }
            }
            return totalCount < limit;
        }
    }
}
