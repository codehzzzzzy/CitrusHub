package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author hzzzzzy
 * @date 2025/1/9
 * @description
 */
@Data
@NoArgsConstructor
@ApiModel("发送对话请求")
public class ChatRequest {

    @ApiModelProperty("消息")
    @NotEmpty
    private String message;

    @ApiModelProperty("消息")
    @NotEmpty
    private String threadSlug;
}
