package com.dabanjia.mqtt.entity;

import org.springframework.lang.NonNull;

/**
 * @ClassName MqttTokenApplyParams
 * @Author Chen Langtao
 * @Date 2021/8/4 12:20
 * @Description
 */
public class TokenApplyRequestParams extends CommonRequestParams {

    /**
     * 系统规定参数。取值：ApplyToken。
     */
    @NonNull
    private String action;
    /**
     * oken的权限类型，取值说明如下：
     *
     * R：只有读权限。
     * W：只有写权限。
     * R,W：同时拥有读和写的权限，R和W之间需要用逗号（,）隔开。
     */
    @NonNull
    private String actions;
    /**
     *
     * Token失效的毫秒时间戳，允许设置的失效最小间隔是60秒，最长为30天。如果输入的取值超过30天，申请接口不会报错，但实际生效时间为30天。。
     */
    @NonNull
    private Long expireTime;
    /**
     * 微消息队列MQTT版实例的ID，一定要和客户端实际使用的实例ID匹配。在控制台实例详情页面获取
     */
    @NonNull
    private String InstanceId;
    /**
     * 微消息队列MQTT版实例所在地域（Region）。
     */
    @NonNull
    private String RegionId;
    /**
     * 资源名称，即MQTT Topic，多个Topic以逗号（,）分隔，每个Token最多运行操作100个资源。当有多个Topic时，需要按照字典顺序排序。
     */
    @NonNull
    private String resources;;

}