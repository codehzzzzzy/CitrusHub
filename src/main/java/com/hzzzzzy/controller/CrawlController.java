package com.hzzzzzy.controller;

import com.hzzzzzy.model.dto.GetPriceRequest;
import com.hzzzzzy.model.dto.SearchSupplyDemandRequest;
import com.hzzzzzy.model.entity.CitrusNews;
import com.hzzzzzy.model.entity.CitrusPrice;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.NewsVO;
import com.hzzzzzy.model.vo.PriceVO;
import com.hzzzzzy.model.vo.SupplyDemandVO;
import com.hzzzzzy.service.CitrusSupplyDemandService;
import com.hzzzzzy.service.CrawlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description SupplyDemandController
 */
@Api(value = "爬虫信息管理", tags = "爬虫信息管理")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/crawl")
public class CrawlController {

    @Autowired
    private CitrusSupplyDemandService supplyDemandService;

    private final CrawlService crawlService;

    @ApiOperation(value = "查看供需信息", tags = "爬虫信息管理")
    @PostMapping("searchSupplyDemand")
    public Result searchSupplyDemand(
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

    @ApiOperation(value = "获取价格信息", tags = "爬虫信息管理")
    @PostMapping("getPrice")
    public Result getPrice(
            @RequestBody
            GetPriceRequest request
    ) {
        PageResult<CitrusPrice> res = crawlService.getPrice(request);
        return new Result<>().success().message("获取成功").data(res);
    }

    @ApiOperation(value = "获取新闻信息", tags = "爬虫信息管理")
    @GetMapping("getNews")
    public Result getNews(
            @RequestParam("current")
            @Parameter(description = "当前页")
                    Integer current,
            @RequestParam("pageSize")
            @Parameter(description = "页容量")
                    Integer pageSize
    ) {
        PageResult<CitrusNews> res = crawlService.getNews(current, pageSize);
        return new Result<>().success().message("获取成功").data(res);
    }

}
