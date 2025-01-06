package com.hzzzzzy.model.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description UserRegisterRequest
 */
@Data
@ApiModel("用户注册请求")
public class UserRegisterRequest {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("重新输入的密码")
    private String checkPassword;

    @ApiModelProperty("用户类别(1:管理员;2:用户;3:专家)")
    private Integer type;
}
