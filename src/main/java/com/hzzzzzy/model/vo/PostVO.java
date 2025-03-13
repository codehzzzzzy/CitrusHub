package com.hzzzzzy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/14
 * @description
 */
@Data
public class PostVO {

    @ApiModelProperty("帖子id")
    private Integer id;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("帖子内容")
    private String context;

    @ApiModelProperty("作者id")
    private Integer authorId;

    @ApiModelProperty("帖子图片url列表")
    private List<String> urlList;

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

    @ApiModelProperty("是否已点赞")
    private Boolean isLiked;
}
