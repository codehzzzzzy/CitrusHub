package com.hzzzzzy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author hzzzzzy
 * @date 2025/1/9
 * @description llm配置文件参数读取类
 */
@Configuration
public class LLMConfiguration {

    public static String AUTHORIZATION;
    public static String MODE;
    public static String WORK_SPACE;
    public static String BASE_URL;

    @Value("${llm.authorization}")
    public void setAuthorization(String param) {
        AUTHORIZATION = param;
    }

    @Value("${llm.mode}")
    public void setMode(String param) {
        MODE = param;
    }

    @Value("${llm.workSpace}")
    public void setWorkSpace(String param) {
        WORK_SPACE = param;
    }

    @Value("${llm.baseURL}")
    public void setBaseURL(String param) {
        BASE_URL = param;
    }
}
