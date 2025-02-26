package com.hzzzzzy.mapper;

import com.hzzzzzy.model.entity.SensitiveWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author hzzzzzy
* @description 针对表【sensitive_word(敏感词表)】的数据库操作Mapper
* @createDate 2025-02-26 15:47:00
* @Entity com.hzzzzzy.model.entity.SensitiveWord
*/
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {

    void insertBatch(List<SensitiveWord> list);
}




