package com.hzzzzzy.sensitive;

import cn.hutool.core.collection.CollUtil;
import com.hzzzzzy.service.SensitiveWordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hzzzzzy
 * @date 2025/2/26
 * @description
 */
@Component
@Slf4j
public class InitSensitiveWord {

    @Autowired
    private SensitiveWordService sensitiveWordService;
    @Autowired
    private ACFilter acFilter;

    @PostConstruct
    public void init() {
        log.info("开始构建敏感词前缀树");
        List<String> list = sensitiveWordService.list().stream().map(item -> item.getWord()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list)){
            acFilter.loadWord(list);
        }
        log.info("构建完成");
    }

}
