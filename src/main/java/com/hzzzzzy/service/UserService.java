package com.hzzzzzy.service;

import com.hzzzzzy.model.dto.UserAltMsgRequest;
import com.hzzzzzy.model.dto.UserRegisterRequest;
import com.hzzzzzy.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.servlet.http.HttpServletRequest;

/**
* @author hzzzzzy
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-01-05 17:57:58
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求
     * @return
     */
    void register(UserRegisterRequest userRegisterRequest);

    /**
     * 登录
     * @param account 账号
     * @param password 密码
     * @return token
     */
    String login(String account, String password);

    /**
     * 登出
     * @param request request
     */
    void logout(HttpServletRequest request);

    /**
     * 修改密码
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param newVerifyPassword
     */
    void alterPwd(HttpServletRequest request, String oldPassword, String newPassword, String newVerifyPassword);

    /**
     * 修改专家信息
     * @param request
     * @param userAltMsgRequest
     */
    void alterMsg(HttpServletRequest request, UserAltMsgRequest userAltMsgRequest);
}
