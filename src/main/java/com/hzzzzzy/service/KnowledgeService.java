package com.hzzzzzy.service;

import com.hzzzzzy.model.dto.AddCategoryRequest;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description
 */
public interface KnowledgeService {

    /**
     * 创建分类
     * @param addCategoryRequest 创建分类请求
     */
    void addCategory(AddCategoryRequest addCategoryRequest);
}
