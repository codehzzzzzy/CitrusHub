package com.hzzzzzy.controller;

import cn.hutool.json.JSONUtil;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.dto.ChatMessage;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.hzzzzzy.constant.CommonConstant.HEADER_TOKEN;
import static com.hzzzzzy.constant.RedisConstant.CHAT_HISTORY_PREFIX;
import static com.hzzzzzy.constant.RedisConstant.USER_LOGIN_TOKEN;

/**
 * @author hzzzzzy
 * @date 2025/1/10
 * @description ChatController
 */
@Api(value = "聊天信息管理", tags = "聊天信息管理")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "获取用户与专家的聊天信息", tags = "聊天信息管理")
    @GetMapping("getChatHistory/{toUserId}")
    public Result getChatHistory(
            @PathVariable("toUserId")
            @Parameter(description = "专家id")
                    Integer toUserId,
            HttpServletRequest request
    ) {
        // 获取当前登录用户
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = redisTemplate.opsForValue().get(USER_LOGIN_TOKEN + token);
        User user = JSONUtil.toBean(jsonUser, User.class);
        if (user == null) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_EMPTY).message("用户未登录"));
        }
        // 确定聊天记录的 Redis 键名
        Integer fromUserId = user.getId();
        String chatKey = CHAT_HISTORY_PREFIX + Math.min(fromUserId, toUserId) + ":" + Math.max(fromUserId, toUserId);
        // 获取聊天记录
        List<String> jsonList = redisTemplate.opsForList().range(chatKey, 0, -1);
        if (jsonList == null || jsonList.isEmpty()) {
            return new Result<List<ChatMessage>>().success().message("暂无聊天记录").data(Collections.emptyList());
        }
        // 转换聊天记录为对象列表
        List<ChatMessage> chatHistory = jsonList.stream()
                .map(json -> JSONUtil.toBean(json, ChatMessage.class))
                .collect(Collectors.toList());

        return new Result<List<ChatMessage>>().success().message("获取成功").data(chatHistory);
    }
}
