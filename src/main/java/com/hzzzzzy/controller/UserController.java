package com.hzzzzzy.controller;

import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.dto.UserAddRequest;
import com.hzzzzzy.model.dto.UserAltMsgRequest;
import com.hzzzzzy.model.dto.UserAltPwdRequest;
import com.hzzzzzy.model.dto.UserRegisterRequest;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description UserController
 */
@Api(value = "用户管理", tags = "用户管理")
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "注册", tags = "用户管理")
    @PostMapping("register")
    public Result register(
            @RequestBody
            @NotEmpty
            UserRegisterRequest userRegisterRequest
    ) {
        userService.register(userRegisterRequest);
        return new Result<>().success().message("注册成功");
    }

    @ApiOperation(value = "登录", tags = "用户管理")
    @PostMapping("/login")
    public Result login(
            @RequestBody
            @NotEmpty
            UserAddRequest userAddRequest
    ){
        String token = userService.login(userAddRequest.getAccount(), userAddRequest.getPassword());
        if ("".equals(token)){
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("token为空"));
        }
        return new Result<String>().success().message("登录成功").data(token);
    }

    @ApiOperation(value = "登出", tags = "用户管理")
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request){
        userService.logout(request);
        return new Result<>().success().message("登出成功");
    }

    @ApiOperation(value = "修改密码", tags = "用户管理")
    @PostMapping("/alterPwd")
    public Result alterPassword(
            @RequestBody
            @NotEmpty
            UserAltPwdRequest userAltPwdRequest,
            HttpServletRequest request
    ){
        userService.alterPwd(request, userAltPwdRequest.getOldPassword(), userAltPwdRequest.getNewPassword(), userAltPwdRequest.getNewVerifyPassword());
        return new Result<>().success().message("修改成功");
    }

    @ApiOperation(value = "修改专家信息", tags = "用户管理")
    @PostMapping("/alterMsg")
    public Result alterMsg(
            @RequestBody
            @NotEmpty
            UserAltMsgRequest userAltMsgRequest,
            HttpServletRequest request
    ){
        userService.alterMsg(request, userAltMsgRequest);
        return new Result<>().success().message("修改专家信息成功");
    }
}
