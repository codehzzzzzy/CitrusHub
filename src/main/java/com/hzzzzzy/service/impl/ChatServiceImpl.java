package com.hzzzzzy.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.constant.RedisConstant;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.dto.ChatHistoryResponse;
import com.hzzzzzy.model.dto.ChatRequest;
import com.hzzzzzy.model.entity.CitrusChat;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.entity.User;
import com.hzzzzzy.service.ChatService;
import com.hzzzzzy.service.CitrusChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import static com.hzzzzzy.config.LLMConfiguration.*;
import static com.hzzzzzy.constant.CommonConstant.HEADER_TOKEN;

/**
 * @author hzzzzzy
 * @date 2025/1/9
 * @description
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CitrusChatService chatService;

    @Override
    public SseEmitter chat(ChatRequest chatRequest) {
        // 设置 SseEmitter 的超时时间
        SseEmitter emitter = new SseEmitter(0L);
        CompletableFuture.runAsync(() -> {
            try {
                // 构建请求体
                JSONObject requestBody = new JSONObject();
                requestBody.put("message", chatRequest.getMessage());
                requestBody.put("mode", MODE);
                // 创建 HTTP 请求
                HttpRequest request = HttpRequest.post(BASE_URL + WORK_SPACE + "/thread/" + chatRequest.getThreadSlug() + "/stream-chat")
                        .header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(requestBody.toString());
                // 异步执行请求
                try (HttpResponse response = request.executeAsync(); BufferedReader reader = new BufferedReader(new InputStreamReader(response.bodyStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        emitter.send(line, MediaType.TEXT_PLAIN);
                    }
                    // 完成数据传输
                    emitter.complete();
                }
            } catch (Exception e) {
                // 捕获异常并发送错误信息
                try {
                    emitter.send("Error occurred: " + e.getMessage(), MediaType.TEXT_PLAIN);
                } catch (Exception sendError) {
                    // 忽略发送错误
                }
                emitter.completeWithError(e);
            }
        });
        // 返回 SseEmitter 实例
        return emitter;
    }

    @Override
    public String createChat(String name, HttpServletRequest request) throws ExecutionException, InterruptedException {
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
        User user = JSONUtil.toBean(jsonUser, User.class);
        Integer userId = user.getId();

        // 构建一个异步任务，处理请求
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            Random random = new Random();
            String threadSlug = String.valueOf(random.nextInt(100000));
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("userId", userId);
            requestBody.put("name", name);
            requestBody.put("slug", threadSlug);
            // 创建 HTTP 请求
            HttpResponse response = HttpRequest.post(BASE_URL + WORK_SPACE + "/thread/new")
                    .header("Authorization", AUTHORIZATION)
                    .body(requestBody.toString())
                    .execute();
            // 判断请求是否成功
            if (response.getStatus() == 200) {
                CitrusChat entity = new CitrusChat();
                entity.setUserId(userId);
                entity.setThreadSlug(threadSlug);
                entity.setName(name);
                chatService.save(entity);
                return threadSlug;
            } else {
                log.info(response.body());
                throw new GlobalException(new Result<>().error(BusinessFailCode.REMOTE_CALL_ERROR).message("创建对话失败"));
            }
        });
        // 异步等待任务执行完成并返回结果
//        try {
            return future.get();
//        } catch (Exception e) {
//            throw new GlobalException(new Result<>().error(BusinessFailCode.REMOTE_CALL_ERROR).message("创建对话失败"));
//        }
    }

    @Override
    public void deleteChat(String threadSlug) {
        // 构建一个异步任务，处理请求
        CompletableFuture.runAsync(() -> {
            // 创建 HTTP 请求
            HttpResponse response = HttpRequest.delete(BASE_URL + WORK_SPACE + "/thread/" + threadSlug)
                    .header("Authorization", AUTHORIZATION)
                    .execute();
            // 判断请求是否成功
            if (response.getStatus() == 200) {
                chatService.remove(new LambdaQueryWrapper<CitrusChat>().eq(CitrusChat::getThreadSlug, threadSlug));
            }else {
                throw new GlobalException(new Result<>().error(BusinessFailCode.REMOTE_CALL_ERROR).message("删除失败: " + response.getStatus()));
            }
        });
    }

    @Override
    public List<String> getChatNames(HttpServletRequest request) {
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
        User user = JSONUtil.toBean(jsonUser, User.class);
        Integer userId = user.getId();
        List<CitrusChat> list = chatService.list(new LambdaQueryWrapper<CitrusChat>().eq(CitrusChat::getUserId, userId));
        if (list.isEmpty()){
            return null;
        }
        return list.stream().map(CitrusChat::getName).collect(Collectors.toList());
    }

    @Override
    public ChatHistoryResponse getChatsByThreadSlug(String threadSlug) {
        // 构建一个异步任务，处理请求
        CompletableFuture<ChatHistoryResponse> future = CompletableFuture.supplyAsync(() -> {
            // 创建 HTTP 请求
            HttpResponse response = HttpRequest.get(BASE_URL + WORK_SPACE + "/thread/" + threadSlug + "/chats")
                    .header("Authorization", AUTHORIZATION)
                    .execute();
            // 判断请求是否成功
            if (response.getStatus() == 200) {
                return JSONUtil.toBean(response.body(), ChatHistoryResponse.class);
            } else {
                throw new GlobalException(new Result<>().error(BusinessFailCode.REMOTE_CALL_ERROR).message("查看对话失败"));
            }
        });
        // 异步等待任务执行完成并返回结果
        try {
            return future.get();
        } catch (Exception e) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.REMOTE_CALL_ERROR).message("查看对话失败"));
        }
    }


}
