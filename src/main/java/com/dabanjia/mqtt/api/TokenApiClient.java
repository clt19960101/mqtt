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
import com.google.common.base.Joiner;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    /**
     * 令牌名称
     */
    public static final String TOKEN_KEY_PREFIX = "mqtt:apply:token:";

    public static final Long FIVE_MINUTE_MICROSECONDS = 300000L;

    /**
     * @param topics 申请的 topic 列表
     * @return 如果申请成功则返回 token 内容
     * @throws ClientException
     */
    public ApplyTokenResponseVO applyToken(List<String> topics, String deviceId)
        throws ClientException {
        //从缓存中获取token
        ApplyTokenResponseVO tokenResponseVO = getTokenFromRedis(deviceId);
        if (Objects.nonNull(tokenResponseVO)) {
            return tokenResponseVO;
        }
        //没有或者已经过期需要向mqtt服务器申请token
        return getTokenFromMqtt(topics, deviceId);
    }

    @Nullable
    private ApplyTokenResponseVO getTokenFromRedis(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }

        String key = redisClient.buildKey(TOKEN_KEY_PREFIX + deviceId);
        String oldToken = redisClient.get(key);
        if(StringUtils.isBlank(oldToken)){
            return null;
        }
        ApplyTokenResponseVO tokenResponseVO = new ApplyTokenResponseVO();
        tokenResponseVO.setToken(oldToken);
        //设置过期时间
        tokenResponseVO.setExpireTime(
            System.currentTimeMillis() + redisClient.getExpire(deviceId,
                TimeUnit.MICROSECONDS));
        return tokenResponseVO;
    }

    @NotNull
    private ApplyTokenResponseVO getTokenFromMqtt(List<String> topics,
        String deviceId)
        throws ClientException {
        // 构建resource
        Collections.sort(topics);
        String resource = Joiner.on(',').join(topics);

        IAcsClient iAcsClient = ClientUtil.getIAcsClient(mqttProperties.getAccessKey(),
            mqttProperties.getSecretKey(), mqttProperties.getRegionId());

        ApplyTokenRequest request = new ApplyTokenRequest();
        request.setInstanceId(mqttProperties.getInstanceId());
        request.setResources(resource);
        request.setActions(mqttProperties.getActions());
        long expireTime = System.currentTimeMillis() + mqttProperties.getExpireTime();
        request.setExpireTime(expireTime);

        ApplyTokenResponse response = iAcsClient.getAcsResponse(request);

        //处理响应
        String token = response.getToken();
        ApplyTokenResponseVO tokenResponseVO = new ApplyTokenResponseVO();
        tokenResponseVO.setToken(token);
        tokenResponseVO.setRequestId(response.getRequestId());
        tokenResponseVO.setExpireTime(expireTime);
        if (StringUtils.isNotBlank(token)) {
            //提前五分钟过期
            long keepTime = mqttProperties.getExpireTime() > FIVE_MINUTE_MICROSECONDS ?
                mqttProperties.getExpireTime() - FIVE_MINUTE_MICROSECONDS
                : mqttProperties.getExpireTime();
            redisClient.set(deviceId, token, keepTime, TimeUnit.MILLISECONDS);
        }
        return tokenResponseVO;
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
}