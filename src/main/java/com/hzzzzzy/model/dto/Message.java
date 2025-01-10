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
@ApiModel("聊天发送类")
public class Message {

    @ApiModelProperty("接收方用户id")
    Integer toUserId;

    @ApiModelProperty("消息内容")
    String content;
}
