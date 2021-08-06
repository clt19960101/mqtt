//package com.dabanjia.mqtt.config;
//
//import com.dabanjia.mqtt.properties.ConnectionProperties;
//import com.dabanjia.mqtt.properties.MqttProperties;
//import com.dabanjia.mqtt.util.ConnectionOptionWrapper;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import javax.annotation.Resource;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @ClassName MqttClientBean
// * @Author Chen Langtao
// * @Date 2021/8/5 10:05
// * @Description
// */
//@Configuration
//@ConditionalOnWebApplication
//@EnableConfigurationProperties({MqttProperties.class, ConnectionProperties.class})
//public class MqttPropertiesConfig {
//
//    @Resource
//    private MqttProperties mqttProperties;
//
//    public static final String TCP_PREFIX = "tcp://";
//    public static final String TCP_SUFFIX = ":1883";
//
//    @Bean(name = "producerMqttClient")
//    public MqttClient getMqttClient() {
//        final MemoryPersistence persistence = new MemoryPersistence();
//        try {
//            MqttClient mqttClient = new MqttClient(
//                TCP_PREFIX + mqttProperties.getEndPoint() + TCP_SUFFIX,
//                mqttProperties.getClientId(), persistence);
//            ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(
//                mqttProperties.getInstanceId(), mqttProperties.getAccessKey(),
//                mqttProperties.getSecretKey(), mqttProperties.getClientId());
//            mqttClient.setTimeToWait(mqttProperties.getTimeToWait());
//            mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
//            return mqttClient;
//        } catch (MqttException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}