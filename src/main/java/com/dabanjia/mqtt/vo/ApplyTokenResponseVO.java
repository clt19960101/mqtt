package com.dabanjia.mqtt.vo;

/**
 * @ClassName ApplyTokenResponseVO
 * @Author Chen Langtao
 * @Date 2021/8/10 18:27
 * @Description
 */
public class ApplyTokenResponseVO {
    private String requestId;
    private String token;
    private Long expireTime;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}