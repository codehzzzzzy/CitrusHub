package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description
 */
@Data
@NoArgsConstructor
@ApiModel("获取价格信息请求")
public class GetPriceRequest {

    @ApiModelProperty(value = "当前页", required = true)
    Integer current;

    @ApiModelProperty(value = "页容量", required = true)
    Integer size;

    @ApiModelProperty("地区")
    String region;

    @ApiModelProperty("种类")
    String category;
}
