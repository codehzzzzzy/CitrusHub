package com.hzzzzzy.controller;

import com.hzzzzzy.model.dto.SearchSupplyDemandRequest;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.SupplyDemandVO;
import com.hzzzzzy.service.CitrusSupplyDemandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description SupplyDemandController
 */
@Api(value = "供需信息管理", tags = "供需信息管理")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/supplyDemand")
public class SupplyDemandController {

    @Autowired
    private CitrusSupplyDemandService supplyDemandService;

    @ApiOperation(value = "查看供需信息", tags = "供需信息管理")
    @PostMapping("search")
    public Result search(
            @RequestBody
            @NotEmpty
            SearchSupplyDemandRequest request,
            @RequestParam("current")
            @Parameter(description = "当前页")
            Integer current,
            @RequestParam("pageSize")
            @Parameter(description = "页容量")
            Integer pageSize
    ) {
        PageResult<SupplyDemandVO> voList = supplyDemandService.search(request, current, pageSize);
        return new Result<>().success().message("查看成功").data(voList);
    }
}
