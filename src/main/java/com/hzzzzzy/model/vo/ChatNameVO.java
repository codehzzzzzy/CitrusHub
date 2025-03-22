package com.hzzzzzy.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/3/22
 * @description
 */
@Data
public class ChatNameVO {


    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("threadSlug")
    private String threadSlug;

    @ApiModelProperty("名称")
    private String name;
}
