package com.hzzzzzy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hzzzzzy
 * @date 2025/1/20
 * @description
 */
@Data
public class CommentVO {

    @ApiModelProperty("评论id")
    private Integer id;

    @ApiModelProperty("帖子id")
    private Integer postId;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("评论内容")
    private String context;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date updateTime;
}
