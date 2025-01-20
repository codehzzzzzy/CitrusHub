package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/14
 * @description
 */
@Data
public class AddCommentRequest {

    @ApiModelProperty("帖子id")
    private Integer postId;

    @ApiModelProperty("评论内容")
    private String context;
}
