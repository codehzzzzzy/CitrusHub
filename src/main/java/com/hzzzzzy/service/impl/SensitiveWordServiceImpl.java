package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.model.entity.SensitiveWord;
import com.hzzzzzy.service.SensitiveWordService;
import com.hzzzzzy.mapper.SensitiveWordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hzzzzzy
* @description 针对表【sensitive_word(敏感词表)】的数据库操作Service实现
* @createDate 2025-02-26 15:47:00
*/
@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord>
    implements SensitiveWordService{

    @Override
    public void insertBatch(List<SensitiveWord> list) {
        baseMapper.insertBatch(list);
    }
}




