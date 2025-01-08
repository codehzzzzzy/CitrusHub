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

    @ApiModelProperty("当前页（默认1，最大5）")
    Integer current;

    @ApiModelProperty("地区（默认不限）")
    String region;

    @ApiModelProperty("种类（默认不限）")
    String category;
}
