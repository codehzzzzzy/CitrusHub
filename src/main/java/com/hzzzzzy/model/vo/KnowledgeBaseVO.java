package com.hzzzzzy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hzzzzzy
 * @date 2025/1/9
 * @description
 */
@Data
public class KnowledgeBaseVO {

    @ApiModelProperty("时间")
    private String date;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("标题")
    private String context;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date updateTime;
}
