package com.hzzzzzy.service;


import com.hzzzzzy.model.dto.GetPriceRequest;
import com.hzzzzzy.model.vo.NewsVO;
import com.hzzzzzy.model.vo.PriceVO;

import java.util.List;

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
    List<PriceVO> getPrice(GetPriceRequest request);

    /**
     * 获取新闻信息
     * @param current
     * @return
     */
    List<NewsVO> getNews(Integer current);
}
