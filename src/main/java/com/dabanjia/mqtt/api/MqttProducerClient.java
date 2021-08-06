package com.dabanjia.mqtt.api;

import com.alibaba.fastjson.JSON;
import com.dabanjia.mqtt.properties.ConnectionProperties;
import com.dabanjia.mqtt.properties.MqttProperties;
import com.dabanjia.mqtt.util.ConnectionOptionWrapper;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @ClassName MqttProducerClient
 * @Author Chen Langtao
 * @Date 2021/8/4 15:00
 * @Description
 */
@Component
public class MqttProducerClient {

    public static final String TCP_PREFIX = "tcp://";
    public static final String TCP_SUFFIX = ":1883";
    public static final MemoryPersistence persistence = new MemoryPersistence();

    @Resource
    MqttProperties mqttProperties;
    @Resource
    ConnectionProperties connectionProperties;

    public void sendSignatureMsg(String topic, String clientId, Object msg)
        throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        MqttClient mqttClient = connectMqttClient(clientId);
        this.sendMessage(topic, mqttClient, JSON.toJSONString(msg));
    }

    public void sendTokenMsg(String topic, String clientId, Map<String, String> tokenData,
        Object msg)
        throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        MqttClient mqttClient = connectMqttClient(clientId, tokenData);
        this.sendMessage(topic, mqttClient, JSON.toJSONString(msg));
    }

    public void sendSignatureP2PMsg(Object msg, String clientId, Map<String, String> tokenData,
        String targetClientId)
        throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        String p2pSendTopic =
            mqttProperties.getParentTopic() + "/p2p/" + targetClientId;
        MqttClient mqttClient = connectMqttClient(clientId, tokenData);
        this.sendMessage(p2pSendTopic, mqttClient, JSON.toJSONString(msg));
    }

    public void sendTokenP2PMsg(Object msg, String clientId, Map<String, String> tokenData,
        String targetClientId) throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        String p2pSendTopic =
            mqttProperties.getParentTopic() + "/p2p/" + targetClientId;
        MqttClient mqttClient = connectMqttClient(clientId, tokenData);
        this.sendMessage(p2pSendTopic, mqttClient, JSON.toJSONString(msg));
    }

    /**
     * 发送普通消息时，topic 必须和接收方订阅的 topic 一致，或者符合通配符匹配规则
     *
     * @param topic
     * @param msg
     */
    private void sendMessage(String topic, MqttClient mqttClient, String msg) throws MqttException {
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(mqttProperties.getQosLevel());
        mqttClient.publish(topic, message);
    }

    /**
     * Signature鉴权 MqttClient
     *
     * @param clientId
     * @return
     */
    private MqttClient connectMqttClient(String clientId)
        throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        MqttClient mqttClient = new MqttClient(
            TCP_PREFIX + mqttProperties.getEndPoint() + TCP_SUFFIX, clientId, persistence);
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(clientId,
            connectionProperties, mqttProperties);
        mqttClient.setTimeToWait(mqttProperties.getTimeToWait());
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        return mqttClient;
    }

    /**
     * Token鉴权 MqttClient
     *
     * @param clientId
     * @return
     */
    private MqttClient connectMqttClient(String clientId,@NonNull Map<String, String> tokenData)
        throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        MqttClient mqttClient = new MqttClient(
            TCP_PREFIX + mqttProperties.getEndPoint() + TCP_SUFFIX, clientId, persistence);
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(tokenData,
            connectionProperties, mqttProperties);
        mqttClient.setTimeToWait(mqttProperties.getTimeToWait());
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        return mqttClient;
    }
}