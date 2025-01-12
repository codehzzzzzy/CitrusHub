package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description
 */
@Data
@NoArgsConstructor
@ApiModel("创建分类请求")
public class AddCategoryRequest {

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类描述")
    private String description;

    @ApiModelProperty("图片url")
    private String url;
}
