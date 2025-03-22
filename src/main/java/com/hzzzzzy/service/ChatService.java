package com.hzzzzzy.service;

import com.hzzzzzy.model.dto.ChatHistoryResponse;
import com.hzzzzzy.model.dto.ChatRequest;
import com.hzzzzzy.model.vo.ChatNameVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author hzzzzzy
 * @date 2025/1/9
 * @description
 */
public interface ChatService {

    /**
     * 对话
     * @param chatRequest
     * @return
     */
    SseEmitter chat(ChatRequest chatRequest);

    /**
     * 创建新对话
     * @param name
     * @param request
     * @return
     */
    String createChat(String name, HttpServletRequest request) throws ExecutionException, InterruptedException;

    /**
     * 删除对话
     * @param threadSlug
     */
    void deleteChat(String threadSlug);

    /**
     * 获取所有对话名称
     * @return
     * @param request
     */
    List<ChatNameVO> getChatNames(HttpServletRequest request);

    /**
     * 获取一个对话下的所有聊天
     * @param threadSlug
     * @return
     */
    ChatHistoryResponse getChatsByThreadSlug(String threadSlug);
}
