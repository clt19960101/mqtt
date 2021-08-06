package com.dabanjia.mqtt.entity;

import java.util.Date;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @ClassName DbjMessage
 * @Author Chen Langtao
 * @Date 2021/8/6 11:08
 * @Description
 */
public class DbjMessage {
    /**
     * msgId
     */
    private String msgId;
    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 消息发送时间
     */
    private Date sendTime;
    /**
     * 具体的业务数据
     */
    private String data;

    public DbjMessage() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}