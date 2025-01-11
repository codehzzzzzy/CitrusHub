package com.hzzzzzy.controller;

import com.hzzzzzy.model.dto.AddCategoryRequest;
import com.hzzzzzy.model.dto.AddKnowledgeRequest;
import com.hzzzzzy.model.dto.UploadKnowledgeRequest;
import com.hzzzzzy.model.entity.KnowledgeBase;
import com.hzzzzzy.model.entity.KnowledgeCategory;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.KnowledgeBaseVO;
import com.hzzzzzy.service.KnowledgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description KnowledgeController
 */
@Api(value = "知识库管理", tags = "知识库管理")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @ApiOperation(value = "创建分类", tags = "知识库管理")
    @PostMapping("addCategory")
    public Result addCategory(
            @RequestBody
            @NotEmpty
            AddCategoryRequest addCategoryRequest
    ) {
        knowledgeService.addCategory(addCategoryRequest);
        return new Result<>().success().message("创建分类成功");
    }

    @ApiOperation(value = "查看所有分类", tags = "知识库管理")
    @GetMapping("getCategory")
    public Result getCategory() {
        List<KnowledgeCategory> categoryList = knowledgeService.getCategory();
        return new Result<>().success().message("查看分类成功").data(categoryList);
    }

    @ApiOperation(value = "创建知识库", tags = "知识库管理")
    @PostMapping("addKnowledge")
    public Result addKnowledge(
            @RequestBody
            @NotEmpty
            AddKnowledgeRequest request
    ) {
        knowledgeService.addKnowledge(request);
        return new Result<>().success().message("创建成功");
    }

    @ApiOperation(value = "更新知识库", tags = "知识库管理")
    @PostMapping("updateKnowledge")
    public Result updateKnowledge(
            @RequestBody
            @NotEmpty
            UploadKnowledgeRequest request
    ) {
        knowledgeService.updateKnowledge(request);
        return new Result<>().success().message("更新成功");
    }

    @ApiOperation(value = "获取某个分类下的所有知识库", tags = "知识库管理")
    @GetMapping("getKnowledge/{categoryId}")
    public Result getKnowledge(
            @PathVariable("categoryId")
            Integer categoryId,
            @RequestParam("current")
            @Parameter(description = "当前页")
            Integer current,
            @RequestParam("pageSize")
            @Parameter(description = "页容量")
            Integer pageSize
    ) {
        PageResult<KnowledgeBaseVO> result = knowledgeService.getKnowledge(categoryId, current, pageSize);
        return new Result<>().success().message("获取成功").data(result);
    }

    @ApiOperation(value = "搜索知识库", tags = "知识库管理")
    @GetMapping("searchKnowledge")
    public Result searchKnowledge(
            @RequestParam(value = "categoryId", required = false)
            @Parameter(description = "分类id")
            Integer categoryId,
            @RequestParam(value = "keyword", required = false)
            @Parameter(description = "关键词")
            String keyword,
            @RequestParam(value = "current")
            @Parameter(description = "当前页")
            Integer current,
            @RequestParam("pageSize")
            @Parameter(description = "页容量")
            Integer pageSize
    ) {
        PageResult<KnowledgeBaseVO> result = knowledgeService.searchKnowledge(categoryId, keyword, current, pageSize);
        return new Result<>().success().message("搜索成功").data(result);
    }

    @ApiOperation(value = "上传md文件", tags = "知识库管理")
    @PostMapping("/upload")
    public Result upload(
            @RequestPart
            @Parameter(description = "md文件")
            MultipartFile file
    ){
        String url = knowledgeService.upload(file);
        return new Result<>().success().message("上传成功").data(url);
    }

    @ApiOperation(value = "删除知识库", tags = "知识库管理")
    @GetMapping("deleteKnowledge/{id}")
    public Result deleteKnowledge(
            @PathVariable("id")
            Integer id
    ) {
        knowledgeService.deleteKnowledge(id);
        return new Result<>().success().message("删除成功");
    }

    @ApiOperation(value = "删除分类", tags = "知识库管理")
    @GetMapping("deleteCategory/{id}")
    public Result deleteCategory(
            @PathVariable("id")
            Integer id
    ) {
        knowledgeService.deleteCategory(id);
        return new Result<>().success().message("删除成功");
    }
}
