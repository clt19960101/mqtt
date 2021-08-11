package com.dabanjia.mqtt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName ConnectionOptions
 * @Author Chen Langtao
 * @Date 2021/8/4 17:23
 * @Description
 */
@ConfigurationProperties("mqtt.connections")
@Component
public class ConnectionProperties {

    /**
     * 重试间隔时间，单位为秒，默认为2秒，不能小于等于0
     */
    private Integer retryIntervalTime;
    /**
     * 连接超时时间，单位为秒，默认为10秒，不能小于等于0
     */
    private Integer connectionTimeout;

    /**
     * 心跳间隔时间，单位为秒，默认为30秒，不能小于等于0
     */
    private Integer keepAliveInterval;
    /**
     * 客户端建立TCP连接后是否关心之前状态 ture:客户端再次上线时，将不再关心之前所有的订阅关系以及离线消息 false:客户端再次上线时，还需要处理之前的离线消息，而之前的订阅关系也会持续生效。
     */
    private Boolean cleanSession;
    /**
     * automaticReconnect设置为true，mqtt连接成功之后，如果因网络抖动导致mqtt断链，SDK会自动重新建联。如果设置为false，则mqtt断开之后，SDK不会自动重新建联，需要用户根据断链状态反馈，自己触发重连逻辑
     */
    private Boolean automaticReconnect;


    public ConnectionProperties() {
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(Integer keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public Boolean getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(Boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public Boolean getAutomaticReconnect() {
        return automaticReconnect;
    }

    public void setAutomaticReconnect(Boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    public Integer getRetryIntervalTime() {
        return retryIntervalTime;
    }

    public void setRetryIntervalTime(Integer retryIntervalTime) {
        this.retryIntervalTime = retryIntervalTime;
    }
}