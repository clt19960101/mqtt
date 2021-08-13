package com.dabanjia.mqtt.api;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.onsmqtt.model.v20200420.ApplyTokenRequest;
import com.aliyuncs.onsmqtt.model.v20200420.ApplyTokenResponse;
import com.aliyuncs.onsmqtt.model.v20200420.QueryTokenRequest;
import com.aliyuncs.onsmqtt.model.v20200420.QueryTokenResponse;
import com.aliyuncs.onsmqtt.model.v20200420.RevokeTokenRequest;
import com.dabanjia.mqtt.properties.MqttProperties;
import com.dabanjia.mqtt.util.ClientUtil;
import com.dabanjia.mqtt.vo.ApplyTokenResponseVO;
import com.dabanjia.witch.doctor.api.util.RedisClient;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * @ClassName TokenApiClient
 * @Author Chen Langtao
 * @Date 2021/8/5 14:42
 * @Description
 */
@AllArgsConstructor
@Component
@Slf4j
public class TokenApiClient {

    @Resource
    MqttProperties mqttProperties;

    @Resource
    RedisClient redisClient;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 令牌名称
     */
    private static final String LIMIT_RATE_NAME = "mqtt:apply:limiter:";
    public static final String TOKEN_KEY_PREFIX = "mqtt:apply:token:";
    public static final String TOKEN_LOCK_PREFIX = "mqtt:apply:lock:";

    public static final Long FIVE_MINUTE_MICROSECONDS = 300000L;

    /**
     * 令牌产生速率
     */
    private static final int LIMIT_RATE = 100;

    /**
     * 单位时间
     */
    private static final int LIMIT_RATE_TIME = 1;


    /**
     * @param topics 申请的 topic 列表
     * @return 如果申请成功则返回 token 内容
     * @throws ClientException
     */
    public ApplyTokenResponseVO applyToken(List<String> topics, String deviceId)
        throws ClientException {
        RLock lock = redissonClient.getLock(TOKEN_LOCK_PREFIX + deviceId);
        try {
            lock.lock();
            ApplyTokenResponseVO tokenResponseVO = new ApplyTokenResponseVO();
            //从缓存中获取token
            if (Objects.nonNull(getTokenFromRedis(deviceId, tokenResponseVO))) {
                return tokenResponseVO;
            }
            //没有或者已经过期需要向mqtt服务器申请token
            return getTokenFromMqtt(topics, tokenResponseVO, deviceId);
        } finally {
            lock.unlock();
        }
    }

    @Nullable
    private ApplyTokenResponseVO getTokenFromRedis(String deviceId,
        ApplyTokenResponseVO tokenResponseVO) {
        if (StringUtils.isNotBlank(deviceId)) {
            String key = redisClient.buildKey(TOKEN_KEY_PREFIX + deviceId);
            String oldToken = redisClient.get(key);
            if (StringUtils.isNotBlank(oldToken)) {
                tokenResponseVO.setToken(oldToken);
                //设置过期时间
                tokenResponseVO.setExpireTime(
                    System.currentTimeMillis() + redisClient.getExpire(deviceId,
                        TimeUnit.MICROSECONDS));
                return tokenResponseVO;
            }
        }
        return null;
    }

    @NotNull
    private ApplyTokenResponseVO getTokenFromMqtt(List<String> topics,
        ApplyTokenResponseVO tokenResponseVO, String deviceId)
        throws ClientException {
        //获取限流器
        RRateLimiter rRateLimiter = getRRateLimiter(deviceId);
        rRateLimiter.acquire();

        //构建resource
        StringBuilder resources = buildResources(topics);

        IAcsClient iAcsClient = ClientUtil.getIAcsClient(mqttProperties.getAccessKey(),
            mqttProperties.getSecretKey(), mqttProperties.getRegionId());

        return applyTokenFromMqtt(tokenResponseVO, deviceId, resources, iAcsClient);
    }

    @NotNull
    private ApplyTokenResponseVO applyTokenFromMqtt(ApplyTokenResponseVO tokenResponseVO,
        String deviceId,
        StringBuilder resources, IAcsClient iAcsClient) throws ClientException {
        ApplyTokenRequest request = new ApplyTokenRequest();
        request.setInstanceId(mqttProperties.getInstanceId());
        request.setResources(resources.toString());
        request.setActions(mqttProperties.getActions());
        long expireTime = System.currentTimeMillis() + mqttProperties.getExpireTime();
        request.setExpireTime(expireTime);

        ApplyTokenResponse response = iAcsClient.getAcsResponse(request);
        //处理响应
        String token = response.getToken();
        if (StringUtils.isNotBlank(token)) {
            //提前五分钟过期
            redisClient.set(deviceId, token,
                mqttProperties.getExpireTime() > FIVE_MINUTE_MICROSECONDS ?
                    mqttProperties.getExpireTime() - FIVE_MINUTE_MICROSECONDS
                    : mqttProperties.getExpireTime(), TimeUnit.MICROSECONDS);
        }
        tokenResponseVO.setToken(response.getToken());
        tokenResponseVO.setRequestId(response.getRequestId());
        tokenResponseVO.setExpireTime(expireTime);
        return tokenResponseVO;
    }

    @NotNull
    private StringBuilder buildResources(List<String> topics) {
        Collections.sort(topics);
        StringBuilder builder = new StringBuilder();
        for (String topic : topics) {
            builder.append(topic).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder;
    }

    /**
     * 提前注销 token，一般在 token 泄露出现安全问题时，提前禁用特定的客户端
     *
     * @param token 禁用的 token 内容
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public void revokeToken(String token) throws ClientException {
        IAcsClient iAcsClient = ClientUtil.getIAcsClient(mqttProperties.getAccessKey(),
            mqttProperties.getSecretKey(), mqttProperties.getRegionId());
        RevokeTokenRequest request = new RevokeTokenRequest();
        request.setInstanceId(mqttProperties.getInstanceId());
        request.setToken(token);
        iAcsClient.getAcsResponse(request);
    }

    /**
     * queryToken
     *
     * @param token
     * @throws ClientException
     */
    public QueryTokenResponse queryToken(String token) throws ClientException {
        IAcsClient iAcsClient = ClientUtil.getIAcsClient(mqttProperties.getAccessKey(),
            mqttProperties.getSecretKey(), mqttProperties.getRegionId());
        QueryTokenRequest request = new QueryTokenRequest();
        request.setInstanceId(mqttProperties.getInstanceId());
        request.setToken(token);
        return iAcsClient.getAcsResponse(request);
    }

    private RRateLimiter getRRateLimiter(String deviceId) {
        String limiterKey = redisClient.buildKey(LIMIT_RATE_NAME) + deviceId;
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiterKey);
        rateLimiter.trySetRate(RateType.OVERALL, LIMIT_RATE, LIMIT_RATE_TIME,
            RateIntervalUnit.SECONDS);
        return rateLimiter;
    }
}