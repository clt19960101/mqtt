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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @ClassName TokenApiClient
 * @Author Chen Langtao
 * @Date 2021/8/5 14:42
 * @Description
 */
@Component
public class TokenApiClient {

    @Resource
    MqttProperties mqttProperties;

    /**
     * @param topics 申请的 topic 列表
     * @return 如果申请成功则返回 token 内容
     * @throws ClientException
     */
    public ApplyTokenResponseVO applyToken(List<String> topics) throws ClientException {
        Collections.sort(topics);
        StringBuilder builder = new StringBuilder();
        for (String topic : topics) {
            builder.append(topic).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        IAcsClient iAcsClient = ClientUtil.getIAcsClient(mqttProperties.getAccessKey(),
            mqttProperties.getSecretKey(), mqttProperties.getRegionId());
        ApplyTokenRequest request = new ApplyTokenRequest();
        request.setInstanceId(mqttProperties.getInstanceId());
        request.setResources(builder.toString());
        request.setActions(mqttProperties.getActions());
        long expireTime = System.currentTimeMillis() + mqttProperties.getExpireTime();
        request.setExpireTime(expireTime);
        ApplyTokenResponse response = iAcsClient.getAcsResponse(request);
        ApplyTokenResponseVO tokenResponseVO = new ApplyTokenResponseVO();
        tokenResponseVO.setToken(response.getToken());
        tokenResponseVO.setRequestId(response.getRequestId());
        tokenResponseVO.setExpireTime(expireTime);
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
     * 校验Token的有效性
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