package com.hzzzzzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzzzzzy.model.entity.CitrusChat;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzzzzzy
* @description 针对表【citrus_chat(对话信息表)】的数据库操作Mapper
* @createDate 2025-01-09 16:06:26
* @Entity com.hzzzzzy.model.entity.CitrusChat
*/
@Mapper
public interface CitrusChatMapper extends BaseMapper<CitrusChat> {

}




