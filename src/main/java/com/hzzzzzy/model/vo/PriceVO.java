package com.hzzzzzy.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description
 */
@Data
public class PriceVO {

    @ApiModelProperty("时间")
    private String date;

    @ApiModelProperty("种类")
    private String category;

    @ApiModelProperty("地区")
    private String region;

    @ApiModelProperty("价格")
    private String price;

    @ApiModelProperty("升降")
    private String lift;
}
