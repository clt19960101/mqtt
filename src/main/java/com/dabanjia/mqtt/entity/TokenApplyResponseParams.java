package com.dabanjia.mqtt.entity;

import org.springframework.lang.NonNull;

/**
 * @ClassName MqttTokenApplyParams
 * @Author Chen Langtao
 * @Date 2021/8/4 12:20
 * @Description
 */
public class TokenApplyResponseParams extends CommonResponseParams {

    /**
     * 公共参数，每个请求的ID都是唯一的。
     */
    private String RequestId;
    /**
     * 服务端返回的Token值。
     */
    private String Token;

}