package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.model.entity.Comment;
import com.hzzzzzy.service.CommentService;
import com.hzzzzzy.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author hzy
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2025-01-20 15:21:22
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




