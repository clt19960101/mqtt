package com.dabanjia.mqtt.properties;

import com.dabanjia.witch.doctor.api.util.RedisClient;
import javax.annotation.Resource;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RateLimiterConfig
 * @Author Chen Langtao
 * @Date 2021/8/13 15:23
 * @Description 限流器初始化
 */
@Configuration
public class RateLimiterConfig {

    /**
     * 令牌产生速率
     */
    private static final int LIMIT_RATE = 100;

    /**
     * 单位时间
     */
    private static final int LIMIT_RATE_TIME = 1;

    /**
     * 令牌名称
     */
    private static final String LIMIT_RATE_NAME = "mqtt:token:limiter";

    @Resource
    private RedisClient redisClient;
    @Resource
    private RedissonClient redissonClient;

    @Bean
    public RRateLimiter getRRateLimiter() {
        String limiterKey = redisClient.buildKey(LIMIT_RATE_NAME);
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiterKey);
        rateLimiter.trySetRate(RateType.OVERALL, LIMIT_RATE, LIMIT_RATE_TIME, RateIntervalUnit.SECONDS);
        return rateLimiter;
    }

}