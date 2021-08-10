package com.dabanjia.mqtt.entity;

import java.util.Date;

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
    private Long sendTime;
    /**
     * 消息发送人
     */
    private Long sendUserId;
    /**
     * 具体的业务数据
     */
    private Object data;

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

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}