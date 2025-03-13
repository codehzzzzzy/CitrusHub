package com.hzzzzzy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.constant.RedisConstant;
import com.hzzzzzy.constant.ReportType;
import com.hzzzzzy.mapper.ReportsMapper;
import com.hzzzzzy.model.dto.AddReportRequest;
import com.hzzzzzy.model.dto.AuditReportRequest;
import com.hzzzzzy.model.entity.*;
import com.hzzzzzy.model.vo.ReportVO;
import com.hzzzzzy.service.*;
import com.hzzzzzy.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.hzzzzzy.constant.CommonConstant.HEADER_TOKEN;
import static com.hzzzzzy.constant.CommonConstant.TRUE;

/**
 * @author hzzzzzy
 * @description 针对表【reports(举报表)】的数据库操作Service实现
 * @createDate 2025-01-11 22:07:11
 */
@Service
public class ReportsServiceImpl extends ServiceImpl<ReportsMapper, Reports>
        implements ReportsService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private PostCommonService postCommonService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    @Override
    public void reportObject(AddReportRequest addReportRequest, HttpServletRequest request) {
        Integer reporterId = getUser(request).getId();
        Reports item = new Reports();
        item.setReporterId(reporterId);
        item.setObjectId(addReportRequest.getObjectId());
        item.setReason(addReportRequest.getReason());
        item.setObjectType(addReportRequest.getObjectType());
        this.save(item);
    }

    @Override
    public PageResult<ReportVO> getReport(Integer current, Integer pageSize, String keyword, Integer status, Integer objectType) {
        LambdaQueryWrapper<Reports> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(!StringUtils.isEmpty(keyword), Reports::getReason, keyword)
                .eq(status != null, Reports::getStatus, status)
                .eq(objectType != null, Reports::getObjectType, objectType);
        List<Reports> reportsList = this.list(queryWrapper);
        if (reportsList == null) {
            return null;
        }
        List<ReportVO> voList = reportsList.stream().map(item -> {
            User reporter = userService.getById(item.getReporterId());
            ReportVO vo = BeanUtil.copyProperties(item, ReportVO.class);
            vo.setReporterAvatar(reporter.getAvatar());
            // 判断类型
            if (item.getObjectType() == ReportType.COMMENT.getType()) {
                vo.setObject(postCommonService.getCommentInfo(item.getObjectId()));
            } else if (item.getObjectType() == ReportType.POST.getType()) {
                vo.setObject(postCommonService.getPostInfo(item.getObjectId()));
            }
            return vo;
        }).collect(Collectors.toList());
        return PageUtil.getPage(voList, current, pageSize);
    }

    @Override
    public void auditReport(AuditReportRequest auditReportRequest, HttpServletRequest request) {
        Integer reportId = auditReportRequest.getReportId();
        Integer flag = auditReportRequest.getFlag();
        String result = auditReportRequest.getResult();

        Reports entity = this.getById(reportId);
        entity.setStatus(2);
        entity.setResult(result);
        this.updateById(entity);
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
        return JSONUtil.toBean(jsonUser, User.class);
    }
}




