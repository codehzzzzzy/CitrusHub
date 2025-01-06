package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("修改专家信息请求")
@Data
@NoArgsConstructor
public class UserAltMsgRequest {

    @ApiModelProperty("专业领域")
    private String expertise;

    @ApiModelProperty("备注")
    private String remark;
}
