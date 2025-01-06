package com.hzzzzzy.websocket;

import cn.hutool.json.JSONUtil;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author hzzzzzy
 * @date 2023/7/26
 * @description WebSocket服务端
 */
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 线程安全Set，存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<WebSocketServer> WEBSOCKET_SET = new CopyOnWriteArraySet<>();

    /**
     * 客户端的连接会话
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        WEBSOCKET_SET.add(this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WEBSOCKET_SET.remove(this);
        log.info("关闭websocket连接");
    }

    /**
     * 连接发生错误调用方法
     *
     * @param error 错误
     */
    @OnError
    public void onError(Throwable error) {
        log.error("websocket连接发生错误:{}",error);
        sendMessage("error");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @return
     */
    @OnMessage
    public void onMessage(String message) {
        if (message.equals("close")){
            this.onClose();
        }else if (message.equals("ping")){
            sendMessage("pong");
        }
    }

    /**
     * 服务器主动推送，发送信息
     */
    public void sendMessage(Object message) {
        try {
            String msg = JSONUtil.toJsonStr(message);
            this.session.getBasicRemote().sendText(JSONUtil.toJsonStr(msg));
        } catch (IOException e) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("websocket推送消息失败"));
        }
    }

    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return WEBSOCKET_SET;
    }
}
