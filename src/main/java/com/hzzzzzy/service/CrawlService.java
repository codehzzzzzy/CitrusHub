package com.hzzzzzy.service;


import com.hzzzzzy.model.dto.GetPriceRequest;
import com.hzzzzzy.model.entity.CitrusNews;
import com.hzzzzzy.model.entity.CitrusPrice;
import com.hzzzzzy.model.entity.PageResult;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description
 */
public interface CrawlService {

    /**
     * 获取价格信息
     * @param request
     * @return
     */
    PageResult<CitrusPrice> getPrice(GetPriceRequest request);

    /**
     * 获取新闻信息
     * @param current
     * @param pageSize
     * @return
     */
    PageResult<CitrusNews> getNews(Integer current, Integer pageSize);
}
