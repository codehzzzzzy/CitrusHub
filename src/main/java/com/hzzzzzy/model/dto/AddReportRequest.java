package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/24
 * @description
 */
@Data
@ApiModel("添加举报请求")
public class AddReportRequest {

	@ApiModelProperty("类型（1:评论,2:帖子）")
	private Integer objectType;

	@ApiModelProperty("对象id")
	private Integer objectId;

	@ApiModelProperty("举报理由")
	private String reason;

}
