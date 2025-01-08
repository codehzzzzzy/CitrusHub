package com.hzzzzzy.service.impl;

import com.hzzzzzy.model.dto.AddCategoryRequest;
import com.hzzzzzy.model.entity.KnowledgeCategory;
import com.hzzzzzy.service.KnowledgeBaseService;
import com.hzzzzzy.service.KnowledgeCategoryService;
import com.hzzzzzy.service.KnowledgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KnowledgeServiceImpl implements KnowledgeService {

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;

    @Override
    public void addCategory(AddCategoryRequest request) {
        KnowledgeCategory category = new KnowledgeCategory();
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        knowledgeCategoryService.save(category);
    }
}
