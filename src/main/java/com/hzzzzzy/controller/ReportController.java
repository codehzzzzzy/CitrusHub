package com.hzzzzzy.controller;

import com.hzzzzzy.constant.ReportType;
import com.hzzzzzy.model.dto.AddReportRequest;
import com.hzzzzzy.model.dto.AuditReportRequest;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.ReportVO;
import com.hzzzzzy.service.ReportsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hzzzzzy
 * @date 2025/1/24
 * @description PostController
 */
@Api(value = "举报管理", tags = "举报管理")
@RestController
@CrossOrigin
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportsService reportsService;

	@ApiOperation(value = "举报", tags = "举报管理")
	@PostMapping("reportObject")
	public Result reportObject(
			@RequestBody
			AddReportRequest addReportRequest,
			HttpServletRequest request
	){
		if (!ReportType.containsType(addReportRequest.getObjectType())){
			return new Result<>().fail().message("类型错误");
		}
		reportsService.reportObject(addReportRequest, request);
		return new Result<>().success().message("举报成功");
	}

	@ApiOperation(value = "查看举报列表", tags = "举报管理")
	@GetMapping("getReport")
	public Result getReport(
			@RequestParam("current")
			@Parameter(description = "当前页")
			Integer current,
			@RequestParam("pageSize")
			@Parameter(description = "页容量")
			Integer pageSize,
			@RequestParam(value = "keyword", required = false)
			@Parameter(description = "搜索举报理由关键词")
			String keyword,
			@RequestParam(value = "status", required = false)
			@Parameter(description = "举报状态（1:待处理;2:已处理）不填则默认全部")
			Integer status,
			@RequestParam(value = "objectType", required = false)
			@Parameter(description = "举报对象类型（1:评论;2:帖子）不填则默认全部")
			Integer objectType
	){
		PageResult<ReportVO> result = reportsService.getReport(current, pageSize, keyword, status, objectType);
		return new Result<>().success().message("查看举报列表").data(result);
	}

	@ApiOperation(value = "审核", tags = "举报管理")
	@PostMapping("auditReport")
	public Result auditReport(
			@RequestBody
			AuditReportRequest auditReportRequest,
			HttpServletRequest request
	){
		reportsService.auditReport(auditReportRequest, request);
		return new Result<>().success().message("审核成功");
	}
}
