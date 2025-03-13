package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.mapper.PostMapper;
import com.hzzzzzy.model.entity.Post;
import com.hzzzzzy.service.PostService;
import org.springframework.stereotype.Service;

/**
* @author hzzzzzy
* @description 针对表【post(帖子表)】的数据库操作Service实现
* @createDate 2025-01-11 22:07:11
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService {

}




