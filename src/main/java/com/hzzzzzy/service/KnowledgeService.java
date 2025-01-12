package com.hzzzzzy.service;

import com.hzzzzzy.model.dto.AddCategoryRequest;
import com.hzzzzzy.model.dto.AddKnowledgeRequest;
import com.hzzzzzy.model.dto.UploadKnowledgeRequest;
import com.hzzzzzy.model.entity.KnowledgeCategory;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.vo.KnowledgeBaseVO;
import org.springframework.web.multipart.MultipartFile;

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
     * 创建/更新知识库
     * @param request
     */
    void addKnowledge(@NotEmpty AddKnowledgeRequest request);

    /**
     * 更新知识库
     * @param entity
     */
    void updateKnowledge(@NotEmpty UploadKnowledgeRequest entity);

    /**
     * 获取知识库
     * @param categoryId
     * @param current
     * @param pageSize
     * @return
     */
    PageResult<KnowledgeBaseVO> getKnowledge(Integer categoryId, Integer current, Integer pageSize);

    /**
     * 上传md文件
     * @param file
     * @return
     */
    String upload(MultipartFile file);

    /**
     * 删除知识库
     * @param id
     */
    void deleteKnowledge(Integer id);

    /**
     * 删除分类
     * @param id
     */
    void deleteCategory(Integer id);

    /**
     * 搜索知识库
     * @param categoryId
     * @param keyword
     * @param current
     * @param pageSize
     * @return
     */
    PageResult<KnowledgeBaseVO> searchKnowledge(Integer categoryId, String keyword, Integer current, Integer pageSize);

    /**
     * 上传分类图片
     * @param file
     * @return
     */
    String uploadCategoryImage(MultipartFile file);

    /**
     * 更新分类
     * @param request
     */
    void updateCategory(KnowledgeCategory request);
}
