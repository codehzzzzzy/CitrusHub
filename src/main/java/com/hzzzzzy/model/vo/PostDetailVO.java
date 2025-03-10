package com.hzzzzzy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hzzzzzy.model.entity.PageResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * @author hzzzzzy
 * @date 2025/1/20
 * @description
 */
@Data
public class PostDetailVO {

    @ApiModelProperty("帖子id")
    private Integer id;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("帖子内容")
    private String context;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("点赞数")
    private Long likeCount;

    @ApiModelProperty("评论数")
    private Long commentCount;

    @ApiModelProperty("评论list")
    private PageResult<CommentVO> commentVOList;
}
