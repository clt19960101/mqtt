package com.dabanjia.mqtt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName MqttProperties
 * @Author Chen Langtao
 * @Date 2021/8/3 18:14
 * @Description matt属性类
 */
@ConfigurationProperties("mqtt.properties")
@Component
public class MqttProperties {

    /**
     * MQ4IOT 实例 ID，购买后控制台获取
     */
    private String instanceId;
    /**
     * 接入点地址，购买 MQ4IOT 实例，且配置完成后即可获取，接入点地址必须填写分配的域名，不得使用 IP 地址直接连接，否则可能会导致客户端异常。
     */
    private String endPoint;
    /**
     * 账号 accesskey，从账号系统控制台获取
     */
    private String accessKey;
    /**
     * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
     */
    private String secretKey;
    /**
     * MQ4IOT 消息的一级 topic，需要在控制台申请才能使用。 如果使用了没有申请或者没有被授权的 topic 会导致鉴权失败，服务端会断开客户端连接。
     */
    private String parentTopic;
    /**
     * 子级 topic
     */
    private String subTopic;
    /**
     * QoS参数代表传输质量，可选0，1，2
     */
    private int qosLevel;
    /**
     * Token的权限类型，取值说明如下：
     * <p>
     * R：只有读权限。 W：只有写权限。 R,W：同时拥有读和写的权限，R和W之间需要用逗号（,）隔开。
     */
    private String actions;
    /**
     * Token失效的毫秒时间戳，允许设置的失效最小间隔是60秒，最长为30天。如果输入的取值超过30天，申请接口不会报错，但实际生效时间为30天。。
     */
    private Long expireTime;

    private String token;
    /**
     * 微消息队列MQTT版实例所在地域（Region）。
     */
    private String regionId;
    /**
     * 话题id
     */
    private String topicId;
    /**
     * 群组id
     */
    private String groupId;
    /**
     * 客户端超时时间
     */
    private int timeToWait;

    public MqttProperties() {
    }


    public String getParentTopic() {
        return parentTopic;
    }

    public void setParentTopic(String parentTopic) {
        this.parentTopic = parentTopic;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public int getQosLevel() {
        return qosLevel;
    }

    public void setQosLevel(int qosLevel) {
        this.qosLevel = qosLevel;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getTimeToWait() {
        return timeToWait;
    }

    public void setTimeToWait(int timeToWait) {
        this.timeToWait = timeToWait;
    }

}