package com.hzzzzzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzzzzzy.model.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzy
* @description 针对表【post(帖子表)】的数据库操作Mapper
* @createDate 2025-01-20 15:21:22
* @Entity generator.domain.Post
*/
@Mapper
public interface PostMapper extends BaseMapper<Post> {

}




