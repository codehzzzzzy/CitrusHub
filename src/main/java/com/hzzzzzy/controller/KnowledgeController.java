package com.hzzzzzy.controller;

import com.hzzzzzy.model.dto.AddCategoryRequest;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.service.KnowledgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;

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
}
