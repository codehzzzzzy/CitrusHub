package com.hzzzzzy.service;

import com.hzzzzzy.model.entity.SensitiveWord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hzzzzzy
* @description 针对表【sensitive_word(敏感词表)】的数据库操作Service
* @createDate 2025-02-26 15:47:00
*/
public interface SensitiveWordService extends IService<SensitiveWord> {

    void insertBatch(List<SensitiveWord> list);
}
