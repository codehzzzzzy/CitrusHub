package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzzzzzy.model.dto.AddCategoryRequest;
import com.hzzzzzy.model.dto.AddknowledgeRequest;
import com.hzzzzzy.model.entity.KnowledgeBase;
import com.hzzzzzy.model.entity.KnowledgeCategory;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.vo.KnowledgeBaseVO;
import com.hzzzzzy.service.KnowledgeBaseService;
import com.hzzzzzy.service.KnowledgeCategoryService;
import com.hzzzzzy.service.KnowledgeService;
import com.hzzzzzy.utils.ListUtil;
import com.hzzzzzy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

    @Override
    public List<KnowledgeCategory> getCategory() {
        return knowledgeCategoryService.list();
    }

    @Override
    public void addKnowledge(AddknowledgeRequest request) {
        KnowledgeBase knowledge = new KnowledgeBase();
        knowledge.setCategoryId(request.getCategoryId());
        knowledge.setTitle(request.getTitle());
        knowledge.setContext(request.getContext());
        knowledgeBaseService.save(knowledge);
    }

    @Override
    public void updateKnowledge(KnowledgeBase entity) {
        knowledgeBaseService.updateById(entity);
    }

    @Override
    public PageResult<KnowledgeBaseVO> getKnowledge(Integer categoryId, Integer current, Integer pageSize) {
        List<KnowledgeBase> entityList = knowledgeBaseService.list(new LambdaQueryWrapper<KnowledgeBase>().eq(KnowledgeBase::getCategoryId, categoryId));
        if (entityList.isEmpty()){
            return null;
        }
        List<KnowledgeBaseVO> voList = ListUtil.entity2VO(entityList, KnowledgeBaseVO.class);
        return PageUtil.getPage(voList, current, pageSize);
    }
}
