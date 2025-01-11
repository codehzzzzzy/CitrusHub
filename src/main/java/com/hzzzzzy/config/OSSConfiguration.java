package com.hzzzzzy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hzzzzzy
 * @date 2025/1/11
 * @description 阿里云oss配置文件参数读取类
 */
@Configuration
public class OSSConfiguration {

    public static String ENDPOINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Value("${oss.endpoint}")
    public void setEndpoint(String endpoint) {
        ENDPOINT = endpoint;
    }

    @Value("${oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        ACCESS_KEY_ID = accessKeyId;
    }

    @Value("${oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        ACCESS_KEY_SECRET = accessKeySecret;
    }

    @Value("${oss.bucketName}")
    public void setBucketName(String bucketName) {
        BUCKET_NAME = bucketName;
    }
}
