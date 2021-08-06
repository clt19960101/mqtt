package com.dabanjia.mqtt.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * @ClassName ClientUtil
 * @Author Chen Langtao
 * @Date 2021/8/4 12:31
 * @Description
 */
public class ClientUtil {

    /**
     *
     * @param accessKey
     * @param secretKey
     * @param regionId
     * @return
     */
    public static IAcsClient getIAcsClient(String accessKey, String secretKey, String regionId) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKey, secretKey);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

}