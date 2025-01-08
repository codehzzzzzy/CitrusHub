package com.hzzzzzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.model.dto.SearchSupplyDemandRequest;
import com.hzzzzzy.model.entity.CitrusSupplyDemand;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.vo.SupplyDemandVO;
import com.hzzzzzy.service.CitrusSupplyDemandService;
import com.hzzzzzy.mapper.CitrusSupplyDemandMapper;
import com.hzzzzzy.utils.ListUtil;
import com.hzzzzzy.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hzzzzzy
* @description 针对表【citrus_supply_demand(供需信息表)】的数据库操作Service实现
* @createDate 2025-01-05 17:57:58
*/
@Service
public class CitrusSupplyDemandServiceImpl extends ServiceImpl<CitrusSupplyDemandMapper, CitrusSupplyDemand>
    implements CitrusSupplyDemandService{

    @Override
    public PageResult<SupplyDemandVO> search(SearchSupplyDemandRequest request, Integer current, Integer pageSize) {
        Integer requireTimePre = request.getRequireTimePre();
        Integer requireTimeAfter = request.getRequireTimeAfter();
        LambdaQueryWrapper<CitrusSupplyDemand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(!StringUtils.isEmpty(request.getTitle()), CitrusSupplyDemand::getTitle, request.getTitle())
                .eq(!StringUtils.isEmpty(request.getCategory()), CitrusSupplyDemand::getCategory, request.getCategory())
                .eq(!StringUtils.isEmpty(request.getRegion()), CitrusSupplyDemand::getRegion, request.getRegion())
                .eq(!StringUtils.isEmpty(request.getReleaseDate()), CitrusSupplyDemand::getReleaseDate, request.getReleaseDate())
                .eq(CitrusSupplyDemand::getType, request.getType());
        if (requireTimePre != null && requireTimeAfter != null) {
            if (requireTimePre < requireTimeAfter) {
                queryWrapper.ge(CitrusSupplyDemand::getRequireTimePre, requireTimePre)
                        .le(CitrusSupplyDemand::getRequireTimeAfter, requireTimeAfter)
                        .apply("require_time_pre < require_time_after"); // 确保 pre < after
            } else if (requireTimePre > requireTimeAfter) {
                queryWrapper.and(wrapper ->
                        wrapper.ge(CitrusSupplyDemand::getRequireTimePre, requireTimePre)
                                .le(CitrusSupplyDemand::getRequireTimeAfter, 12)
                ).and(wrapper ->
                        wrapper.ge(CitrusSupplyDemand::getRequireTimePre, 1)
                                .le(CitrusSupplyDemand::getRequireTimeAfter, requireTimeAfter)
                );
            }
        }
        List<CitrusSupplyDemand> entityList = this.list(queryWrapper);
        if (entityList.isEmpty()){
            return null;
        }
        return PageUtil.getPage(ListUtil.entity2VO(entityList, SupplyDemandVO.class), current, pageSize);
    }


}




