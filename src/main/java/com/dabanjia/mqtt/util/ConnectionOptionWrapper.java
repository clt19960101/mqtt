package com.dabanjia.mqtt.util;

import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1;

import com.dabanjia.mqtt.properties.ConnectionProperties;
import com.dabanjia.mqtt.properties.MqttProperties;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 工具类：负责封装 MQ4IOT 客户端的初始化参数设置
 */
@Component
public class ConnectionOptionWrapper {

    public static final String TOKEN_PREFIX = "Token|";
    public static final String SIGNATURE_PREFIX = "Signature|";

    /**
     * 内部连接参数
     */
    private MqttConnectOptions mqttConnectOptions;

    public ConnectionOptionWrapper() {
    }

    /**
     * Token 鉴权模式下构造方法
     *
     * @param tokenData 客户端使用的 Token 参数，仅在 Token 鉴权模式下需要设置
     */
    public ConnectionOptionWrapper(Map<String, String> tokenData,
        ConnectionProperties connectionProperties, MqttProperties mqttProperties) {
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(
            TOKEN_PREFIX + mqttProperties.getAccessKey() + "|" + mqttProperties.getInstanceId());
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : tokenData.entrySet()) {
            builder.append(entry.getKey()).append("|").append(entry.getValue()).append("|");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        mqttConnectOptions.setPassword(builder.toString().toCharArray());
        mqttConnectOptions.setCleanSession(connectionProperties.getCleanSession());
        mqttConnectOptions.setKeepAliveInterval(connectionProperties.getKeepAliveInterval());
        mqttConnectOptions.setAutomaticReconnect(connectionProperties.getAutomaticReconnect());
        mqttConnectOptions.setMqttVersion(MQTT_VERSION_3_1_1);
        mqttConnectOptions.setConnectionTimeout(connectionProperties.getConnectionTimeout());
    }

    /**
     * Signature 鉴权模式下构造方法
     *
     * @param clientId MQ4IOT clientId，由业务系统分配
     */
    public ConnectionOptionWrapper(String clientId, ConnectionProperties connectionProperties,
        MqttProperties mqttProperties)
        throws NoSuchAlgorithmException, InvalidKeyException {
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(SIGNATURE_PREFIX + mqttProperties.getAccessKey() + "|"
            + mqttProperties.getInstanceId());
        mqttConnectOptions.setPassword(
            Tools.macSignature(clientId, mqttProperties.getSecretKey()).toCharArray());
        mqttConnectOptions.setCleanSession(connectionProperties.getCleanSession());
        mqttConnectOptions.setKeepAliveInterval(connectionProperties.getKeepAliveInterval());
        mqttConnectOptions.setAutomaticReconnect(connectionProperties.getAutomaticReconnect());
        mqttConnectOptions.setMqttVersion(MQTT_VERSION_3_1_1);
        mqttConnectOptions.setConnectionTimeout(connectionProperties.getConnectionTimeout());
    }

    public MqttConnectOptions getMqttConnectOptions() {
        return mqttConnectOptions;
    }
}
