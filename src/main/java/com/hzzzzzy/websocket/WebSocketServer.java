package com.hzzzzzy.websocket;

import cn.hutool.json.JSONUtil;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.dto.ChatMessage;
import com.hzzzzzy.model.dto.Message;
import com.hzzzzzy.model.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import static com.hzzzzzy.constant.RedisConstant.CHAT_HISTORY_PREFIX;

/**
 * @author hzzzzzy
 * @date 2023/7/26
 * @description WebSocket服务端 - 支持1对1聊天并记录历史消息
 */
@ServerEndpoint(value = "/websocket/{fromUserId}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 时间格式化器
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 线程安全Map，存放每个用户的WebSocket对象
     */
    private static final ConcurrentHashMap<Integer, WebSocketServer> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 客户端的连接会话
     */
    private Session session;

    /**
     * 当前连接用户ID
     */
    private Integer userId;

    /**
     * Redis操作模板
     */
    private static StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        WebSocketServer.redisTemplate = redisTemplate;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session WebSocket会话
     * @param fromUserId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("fromUserId") Integer fromUserId) {
        this.session = session;
        try {
            userId = fromUserId;
            WEBSOCKET_MAP.put(fromUserId, this);
            log.info("用户连接成功，userId: {}", userId);
            sendMessage("连接成功");
        } catch (Exception e) {
            log.error("连接建立失败: {}", e.getMessage());
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("连接建立失败"));
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (userId != null) {
            WEBSOCKET_MAP.remove(userId);
            log.info("用户断开连接，userId: {}", userId);
        }
    }

    /**
     * 连接发生错误调用方法
     *
     * @param error 错误
     */
    @OnError
    public void onError(Throwable error) {
        log.error("WebSocket连接发生错误: {}", error.getMessage());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息（需包含目标用户ID和消息内容）
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("收到用户消息，userId: {}, message: {}", userId, message);
        try {
            Message msg = JSONUtil.toBean(message, Message.class);
            Integer toUserId = msg.getToUserId();
            String content = msg.getContent();
            String timestamp = LocalDateTime.now().format(FORMATTER);

            // 保存消息到Redis
            saveChatHistory(userId, toUserId, content, timestamp);

            // 获取目标用户的WebSocket连接并发送消息
            WebSocketServer target = WEBSOCKET_MAP.get(toUserId);
            if (target != null) {
                target.sendMessage(content);
            } else {
                sendMessage("用户不存在或不在线");
            }
        } catch (Exception e) {
            log.error("处理消息时发生错误: {}", e.getMessage());
            sendMessage("消息格式错误");
        }
    }

    private void saveChatHistory(Integer fromUserId, Integer toUserId, String content, String timestamp) {
        // 确保两人之间的聊天记录使用相同的键
        String chatKey = CHAT_HISTORY_PREFIX + Math.min(fromUserId, toUserId) + ":" + Math.max(fromUserId, toUserId);
        // 构造消息对象
        ChatMessage message = new ChatMessage();
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setContent(content);
        message.setTimestamp(timestamp);
        // 将消息对象转换为JSON字符串
        String jsonStr = JSONUtil.toJsonStr(message);
        // 保存到Redis列表中
        redisTemplate.opsForList().rightPush(chatKey, jsonStr);
        log.info("保存聊天记录，key: {}, message: {}", chatKey, jsonStr);
    }

    /**
     * 服务器主动推送消息
     *
     * @param message 要发送的消息
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("WebSocket推送消息失败: {}", e.getMessage());
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("WebSocket推送消息失败"));
        }
    }
}
