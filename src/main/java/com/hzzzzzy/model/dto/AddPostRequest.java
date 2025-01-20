package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/14
 * @description
 */
@Data
@ApiModel("添加帖子请求")
public class AddPostRequest {

    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("帖子内容")
    private String context;
}