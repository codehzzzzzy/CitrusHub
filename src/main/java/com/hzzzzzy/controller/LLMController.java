package com.hzzzzzy.controller;

import com.hzzzzzy.model.dto.ChatHistoryResponse;
import com.hzzzzzy.model.dto.ChatRequest;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/9
 * @description ChatController
 */
@Api(value = "大模型对话管理", tags = "大模型对话管理")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/llm")
public class LLMController {

    private final ChatService chatService;

    @ApiOperation(value = "对话", tags = "大模型对话管理")
    @PostMapping(value = "chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(
            @RequestBody
            @NotEmpty
            ChatRequest chatRequest
    ) {
        return chatService.chat(chatRequest);
    }

    @ApiOperation(value = "创建新对话", tags = "大模型对话管理")
    @PostMapping("createChat/{name}")
    public Result createChat(
            @PathVariable("name")
            String name,
            HttpServletRequest request
    ) {
        String threadSlug = chatService.createChat(name, request);
        return new Result<>().success().message("创建成功").data(threadSlug);
    }

    @ApiOperation(value = "删除对话", tags = "大模型对话管理")
    @PostMapping("deleteChat/{threadSlug}")
    public Result deleteChat(@PathVariable("threadSlug") String threadSlug) {
        chatService.deleteChat(threadSlug);
        return new Result<>().success().message("删除成功");
    }

    @ApiOperation(value = "获取所有对话名称", tags = "大模型对话管理")
    @GetMapping("getChatNames")
    public Result getChatNames(HttpServletRequest request) {
        List<String> chatNames = chatService.getChatNames(request);
        return new Result<>().success().data(chatNames);
    }

    @ApiOperation(value = "获取一个对话下的所有聊天", tags = "大模型对话管理")
    @GetMapping("getChatsByThreadSlug/{threadSlug}")
    public Result getChatsByThreadSlug(@PathVariable("threadSlug") String threadSlug) {
        ChatHistoryResponse response = chatService.getChatsByThreadSlug(threadSlug);
        return new Result<>().success().data(response);
    }
}
