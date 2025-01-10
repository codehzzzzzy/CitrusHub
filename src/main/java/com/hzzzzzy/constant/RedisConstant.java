package com.hzzzzzy.constant;

/**
 * @author hzzzzzy
 * @create 2025/1/6
 * @description 业务枚举类
 */
public interface RedisConstant {

    /**
     * 用户登录token
     */
    String USER_LOGIN_TOKEN="login:token:";

    /**
     * 用户登录token过期时间
     */
    Integer USER_LOGIN_TOKEN_EXPIRE = 60 * 60 * 24 * 7;

    /**
     * 聊天历史记录key前缀
     */
    String CHAT_HISTORY_PREFIX = "chat:history:";
}
