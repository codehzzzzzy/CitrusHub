package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Post;
import com.hzzzzzy.service.PostService;
import com.hzzzzzy.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author hzy
* @description 针对表【post(帖子表)】的数据库操作Service实现
* @createDate 2025-01-20 15:21:22
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

}




