package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户修改密码请求")
@Data
@NoArgsConstructor
public class UserAltPwdRequest {

    @ApiModelProperty("原密码")
    String oldPassword;

    @ApiModelProperty("新密码")
    String newPassword;

    @ApiModelProperty("新密码确认密码")
    String newVerifyPassword;
}
