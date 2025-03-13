package com.hzzzzzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzzzzzy.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzzzzzy
* @description 针对表【comment(评论表)】的数据库操作Mapper
* @createDate 2025-01-11 22:07:11
* @Entity com.hzzzzzy.model.entity.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}




