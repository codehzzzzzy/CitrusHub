package com.hzzzzzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzzzzzy.model.dto.AddReportRequest;
import com.hzzzzzy.model.dto.AuditReportRequest;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Reports;
import com.hzzzzzy.model.vo.ReportVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author hzzzzzy
* @description 针对表【reports(举报表)】的数据库操作Service
* @createDate 2025-01-11 22:07:11
*/
public interface ReportsService extends IService<Reports> {

	void reportObject(AddReportRequest addReportRequest, HttpServletRequest request);

	PageResult<ReportVO> getReport(Integer current, Integer pageSize, String keyword, Integer status, Integer objectType);

	void auditReport(AuditReportRequest auditReportRequest, HttpServletRequest request);
}
