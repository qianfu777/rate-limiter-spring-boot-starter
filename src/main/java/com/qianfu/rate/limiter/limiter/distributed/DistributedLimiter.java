package com.qianfu.rate.limiter.limiter.distributed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分布式限流器
 *
 * @author qianfu
 * @date 2019/11/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributedLimiter {
    private int max;
    private String type;
}
