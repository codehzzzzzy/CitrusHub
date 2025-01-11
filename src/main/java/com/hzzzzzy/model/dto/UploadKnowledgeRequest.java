package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author hzzzzzy
 * @date 2025/1/9
 * @description
 */
@Data
@NoArgsConstructor
@ApiModel("更新知识库请求")
public class UploadKnowledgeRequest {

    @ApiModelProperty("知识库id")
    @NotEmpty
    private Integer knowledgeId;

    @ApiModelProperty("分类id")
    @NotEmpty
    private Integer categoryId;

    @ApiModelProperty("标题")
    @NotEmpty
    private String title;

    @ApiModelProperty("md链接")
    @NotEmpty
    private String url;
}
