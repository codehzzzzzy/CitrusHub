package com.hzzzzzy.service;

import com.hzzzzzy.model.dto.AddCategoryRequest;
import com.hzzzzzy.model.dto.AddknowledgeRequest;
import com.hzzzzzy.model.entity.KnowledgeBase;
import com.hzzzzzy.model.entity.KnowledgeCategory;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.vo.KnowledgeBaseVO;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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

    /**
     * 查看所有分类
     * @return
     */
    List<KnowledgeCategory> getCategory();

    /**
     * 创建知识库
     * @param request
     */
    void addKnowledge(@NotEmpty AddknowledgeRequest request);

    /**
     * 更新知识库
     * @param entity
     */
    void updateKnowledge(KnowledgeBase entity);

    /**
     * 获取知识库
     * @param categoryId
     * @param current
     * @param pageSize
     * @return
     */
    PageResult<KnowledgeBaseVO> getKnowledge(Integer categoryId, Integer current, Integer pageSize);
}
