package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description
 */
@Data
@NoArgsConstructor
@ApiModel("查询供需信息请求")
public class SearchSupplyDemandRequest {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("供应/求购")
    @NotEmpty
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
}
