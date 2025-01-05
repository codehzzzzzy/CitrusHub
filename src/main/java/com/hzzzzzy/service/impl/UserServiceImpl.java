package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.model.entity.User;
import com.hzzzzzy.service.UserService;
import com.hzzzzzy.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author hzzzzzy
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-01-05 17:57:58
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




