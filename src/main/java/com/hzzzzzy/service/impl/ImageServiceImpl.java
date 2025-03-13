package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.mapper.ImageMapper;
import com.hzzzzzy.model.entity.Image;
import com.hzzzzzy.service.ImageService;
import org.springframework.stereotype.Service;

/**
 * @author hzy
 * @description 针对表【image(帖子图片表)】的数据库操作Service实现
 * @createDate 2025-01-24 14:32:57
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
		implements ImageService {

}