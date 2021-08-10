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
    private int retryIntervalTime;
    /**
     * 连接超时时间，单位为秒，默认为10秒，不能小于等于0
     */
    private int connectionTimeout;
    /**
     * 心跳间隔时间，单位为秒，默认为30秒，不能小于等于0
     */
    private int keepAliveInterval;
    /**
     * 客户端建立TCP连接后是否关心之前状态 ture:客户端再次上线时，将不再关心之前所有的订阅关系以及离线消息 false:客户端再次上线时，还需要处理之前的离线消息，而之前的订阅关系也会持续生效。
     */
    private Boolean cleanSession = false;

    private Boolean automaticReconnect;


    public ConnectionProperties() {
    }

    public int getRetryIntervalTime() {
        return retryIntervalTime;
    }

    public void setRetryIntervalTime(int retryIntervalTime) {
        this.retryIntervalTime = retryIntervalTime;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(int keepAliveInterval) {
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
}