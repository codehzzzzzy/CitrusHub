package com.hzzzzzy.mapper;

import com.hzzzzzy.model.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzy
* @description 针对表【comment(评论表)】的数据库操作Mapper
* @createDate 2025-01-20 15:21:22
* @Entity com.hzzzzzy.model.entity.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}




