package com.hzzzzzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzzzzzy.model.entity.Image;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzy
* @description 针对表【image(帖子图片表)】的数据库操作Mapper
* @createDate 2025-01-24 14:32:57
* @Entity com.hzzzzzy.model.entity.Image
*/
@Mapper
public interface ImageMapper extends BaseMapper<Image> {

}




