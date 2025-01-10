package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description
 */
@Data
@NoArgsConstructor
@ApiModel("聊天信息类")
public class ChatMessage {

    @ApiModelProperty("内容")
    String content;

    @ApiModelProperty("发送者")
    Integer fromUserId;

    @ApiModelProperty("接收者")
    Integer toUserId;

    @ApiModelProperty("时间戳")
    String timestamp;
}
