package com.hzzzzzy.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description UserAddRequest
 */
@Data
@NoArgsConstructor
@ApiModel("登录请求信息")
public class UserAddRequest {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;
}
