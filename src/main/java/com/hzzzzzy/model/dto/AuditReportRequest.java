package com.hzzzzzy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/2/10
 * @description
 */
@Data
@ApiModel("审核请求")
public class AuditReportRequest {

	@ApiModelProperty("举报id")
	private Integer reportId;

	@ApiModelProperty("举报成功:1;举报失败:0")
	private Integer flag;

	@ApiModelProperty("处理结果")
	private String result;
}
