package com.hzzzzzy.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.dto.AddCategoryRequest;
import com.hzzzzzy.model.dto.AddKnowledgeRequest;
import com.hzzzzzy.model.dto.UploadKnowledgeRequest;
import com.hzzzzzy.model.entity.KnowledgeBase;
import com.hzzzzzy.model.entity.KnowledgeCategory;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.KnowledgeBaseVO;
import com.hzzzzzy.service.KnowledgeBaseService;
import com.hzzzzzy.service.KnowledgeCategoryService;
import com.hzzzzzy.service.KnowledgeService;
import com.hzzzzzy.utils.ListUtil;
import com.hzzzzzy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.hzzzzzy.config.OSSConfiguration.*;

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
    public void addKnowledge(AddKnowledgeRequest request) {
        KnowledgeBase knowledge = new KnowledgeBase();
        knowledge.setCategoryId(request.getCategoryId());
        knowledge.setTitle(request.getTitle());
        knowledge.setUrl(request.getUrl());
        knowledgeBaseService.save(knowledge);
    }

    @Override
    public void updateKnowledge(UploadKnowledgeRequest entity) {
        KnowledgeBase knowledge = new KnowledgeBase();
        knowledge.setId(entity.getKnowledgeId());
        knowledge.setCategoryId(entity.getCategoryId());
        knowledge.setTitle(entity.getTitle());
        knowledge.setUrl(entity.getUrl());
        knowledgeBaseService.updateById(knowledge);
    }

    @Override
    public PageResult<KnowledgeBaseVO> getKnowledge(Integer categoryId, Integer current, Integer pageSize) {
        List<KnowledgeBase> entityList = knowledgeBaseService.list(new LambdaQueryWrapper<KnowledgeBase>().eq(KnowledgeBase::getCategoryId, categoryId));
        if (entityList.isEmpty()){
            return null;
        }
        return PageUtil.getPage(ListUtil.entity2VO(entityList, KnowledgeBaseVO.class), current, pageSize);
    }

    @Override
    public String upload(MultipartFile file) {
        // 获取文件名称
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + new Date().getTime() + ".md";
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream());
            ossClient.putObject(putObjectRequest);
            ossClient.setObjectAcl(BUCKET_NAME, fileName, CannedAccessControlList.PublicRead);
            // 构造永久URL
            String url = "https://" + BUCKET_NAME + "." + ENDPOINT + "/" + fileName;
            return url;
        } catch (IOException e) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.FILE_UPLOAD_ERROR).message("文件上传失败"));
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void deleteKnowledge(Integer id) {
        boolean flag = knowledgeBaseService.removeById(id);
        if (!flag) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("删除失败"));
        }
    }

    @Override
    public void deleteCategory(Integer id) {
        if (knowledgeBaseService.count(new LambdaQueryWrapper<KnowledgeBase>().eq(KnowledgeBase::getCategoryId, id)) > 0) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("该分类下存在文章，请先删除该分类下的所有文章"));
        }
        knowledgeCategoryService.removeById(id);
    }

    @Override
    public PageResult<KnowledgeBaseVO> searchKnowledge(Integer categoryId, String keyword, Integer current, Integer pageSize) {
        LambdaQueryWrapper<KnowledgeBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, KnowledgeBase::getCategoryId, categoryId)
                .like(!StringUtils.isEmpty(keyword), KnowledgeBase::getTitle, keyword);
        List<KnowledgeBase> entityList = knowledgeBaseService.list(queryWrapper);
        if (entityList.isEmpty()){
            return null;
        }
        return PageUtil.getPage(ListUtil.entity2VO(entityList, KnowledgeBaseVO.class), current, pageSize);
    }
}
