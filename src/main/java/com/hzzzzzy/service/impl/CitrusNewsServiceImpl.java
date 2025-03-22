package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.model.entity.CitrusNews;
import com.hzzzzzy.service.CitrusNewsService;
import com.hzzzzzy.mapper.CitrusNewsMapper;
import org.springframework.stereotype.Service;

/**
* @author hzzzzzy
* @description 针对表【citrus_news(价格信息表)】的数据库操作Service实现
* @createDate 2025-03-13 15:30:49
*/
@Service
public class CitrusNewsServiceImpl extends ServiceImpl<CitrusNewsMapper, CitrusNews>
    implements CitrusNewsService{

}




