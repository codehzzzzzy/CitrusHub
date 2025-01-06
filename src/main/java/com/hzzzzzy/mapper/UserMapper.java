package com.hzzzzzy.mapper;

import com.hzzzzzy.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzzzzzy
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2025-01-05 17:57:58
* @Entity com.hzzzzzy.model.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




