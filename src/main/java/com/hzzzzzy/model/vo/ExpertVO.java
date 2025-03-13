package com.hzzzzzy.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/10
 * @description
 */
@Data
public class ExpertVO {

    @ApiModelProperty("专家id")
    Integer id;

    @ApiModelProperty("专业领域")
    String expertise;

    @ApiModelProperty("备注")
    String remark;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("是否上线")
    private Boolean isOnline;
}
