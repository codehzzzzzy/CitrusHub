package com.hzzzzzy.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description
 */
@Data
public class SupplyDemandVO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("供应/求购")
    private String type;

    @ApiModelProperty("柑橘种类")
    private String category;

    @ApiModelProperty("地区")
    private String region;

    @ApiModelProperty("发布日期 mm-dd")
    private String releaseDate;

    @ApiModelProperty("需求日期-前")
    private Integer requireTimePre;

    @ApiModelProperty("需求日期-后")
    private Integer requireTimeAfter;

    @ApiModelProperty("图片链接")
    private String imageUrl;

    @ApiModelProperty("跳转链接")
    private String url;
}
