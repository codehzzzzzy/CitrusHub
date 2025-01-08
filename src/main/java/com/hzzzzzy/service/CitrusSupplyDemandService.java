package com.hzzzzzy.service;

import com.hzzzzzy.model.dto.SearchSupplyDemandRequest;
import com.hzzzzzy.model.entity.CitrusSupplyDemand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.vo.SupplyDemandVO;

/**
* @author hzzzzzy
* @description 针对表【citrus_supply_demand(供需信息表)】的数据库操作Service
* @createDate 2025-01-05 17:57:58
*/
public interface CitrusSupplyDemandService extends IService<CitrusSupplyDemand> {

    /**
     * 搜索供需信息
     * @param request 请求
     * @param current
     * @param pageSize
     * @return
     */
    PageResult<SupplyDemandVO> search(SearchSupplyDemandRequest request, Integer current, Integer pageSize);
}
