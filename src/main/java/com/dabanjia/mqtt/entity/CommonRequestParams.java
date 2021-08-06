package com.dabanjia.mqtt.entity;

import org.springframework.lang.NonNull;

/**
 * @ClassName CommonRequestParams
 * @Author Chen Langtao
 * @Date 2021/8/3 20:08
 * @Description 公共请求参数
 */
public class CommonRequestParams {

    /**
     * 返回消息的格式 JSON（默认值）|XML
     */
    private String format;
    /**
     * API版本号，使用YYYY-MM-DD日期格式。取值：2020-04-20
     */
    @NonNull
    private String version;
    /**
     * 访问服务使用的密钥ID。
     */
    @NonNull
    private String accessKeyId;
    /**
     * 签名结果串
     */
    @NonNull
    private String signature;
    /**
     * 签名方式，取值：HMAC-SHA1
     */
    @NonNull
    private String signatureMethod;
    /**
     * 请求的时间戳，为日期格式。使用UTC时间按照ISO8601标准，格式为YYYY-MM-DDThh:mm:ssZ。
     * 例如，北京时间2013年01月10日20点0分0秒，表示为2013-01-10T12:00:00Z。
     */
    @NonNull
    private String timestamp;
    /**
     * 签名算法版本，取值：1.0
     */
    @NonNull
    private String signatureVersion;
    /**
     * 唯一随机数，用于防止网络重放攻击。
     * 在不同请求间要使用不同的随机数值。
     */
    @NonNull
    private String signatureNonce;

    /**
     * 本次API请求访问到的资源拥有者账户，即登录用户名。
     */
    private String resourceOwnerAccount;

}