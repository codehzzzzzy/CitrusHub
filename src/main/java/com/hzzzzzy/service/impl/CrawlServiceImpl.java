package com.hzzzzzy.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.dto.GetPriceRequest;
import com.hzzzzzy.model.entity.CitrusNews;
import com.hzzzzzy.model.entity.CitrusPrice;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.NewsVO;
import com.hzzzzzy.service.CitrusNewsService;
import com.hzzzzzy.service.CitrusPriceService;
import com.hzzzzzy.service.CrawlService;
import com.hzzzzzy.utils.WebClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hzzzzzy.utils.PageUtil.getPage;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlServiceImpl implements CrawlService {

//    private static final String PRICE_BASE_URL = "https://www.cnhnb.com/hangqing/cdlist-2001686-0-0-0-0-1/";
//    private static final String NEWS_BASE_URL = "https://news.foodmate.net/search.php?moduleid=21&spread=0&kw=%E6%9F%91%E6%A9%98&page=";
//    public static String replaceLastNumber(String input, String replacement) {
//        return input.replaceAll("\\d+(?!.*\\d)", replacement);
//    }
//    private static HtmlAnchor findAnchorByRegion(HtmlPage page, String region) {
//        for (HtmlAnchor anchor : page.getAnchors()) {
//            if (region.equals(anchor.asText().trim())) {
//                return anchor;
//            }
//        }
//        return null;
//    }

//    @Override
//    public List<PriceVO> getPrice(GetPriceRequest request) {
//        String region = request.getRegion();
//        String category = request.getCategory();
//        Integer current = request.getCurrent();
//        List<PriceVO> voList = new ArrayList<>();
//        WebClient webClient = WebClientUtils.getWebClient();
//        HtmlPage page;
//        try {
//            page = webClient.getPage(PRICE_BASE_URL);
//            // 地区不为空
//            if (region != null && !region.isEmpty()) {
//                HtmlAnchor anchor = findAnchorByRegion(page, region);
//                if (anchor != null) {
//                    page = anchor.click();
//                }
//            }
//            // 种类不为空
//            if (category != null && !category.isEmpty()) {
//                HtmlAnchor anchor = findAnchorByRegion(page, category);
//                if (anchor != null) {
//                    page = anchor.click();
//                }
//            }
//            // 页数不为空
//            if (current != null) {
//                String url = replaceLastNumber(page.getUrl().toString(), String.valueOf(current));
//                page = webClient.getPage(url);
//            }
//        } catch (IOException e) {
//            log.error("获取价格信息URL失败");
//            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("获取价格信息URL失败"));
//        }
//
//        try {
//            Document document = Jsoup.parse(page.asXml());
//            Elements listItems = document.select("li.market-list-item");
//            if (listItems.isEmpty()) {
//                return null;
//            }
//            for (Element listItem : listItems) {
//                Element date = listItem.select("span.time").first();
//                Element categoryElement = listItem.select("span.product").first();
//                Element regionElement = listItem.select("span.place").first();
//                Element price = listItem.select("span.price").first();
//                Element lift = listItem.select("span.lifting.risecolor").first();
//                PriceVO priceVO = new PriceVO();
//                priceVO.setDate(date.text());
//                priceVO.setCategory(categoryElement.text());
//                priceVO.setRegion(regionElement.text());
//                priceVO.setPrice(price.text());
//                if (lift != null){
//                    priceVO.setLift(lift.text());
//                }
//                voList.add(priceVO);
//            }
//        } catch (Exception e) {
//            log.error("解析价格信息页面失败", e);
//            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("解析价格信息页面失败"));
//        } finally {
//            // 确保释放资源
//            webClient.close();
//            log.info("WebClient 已关闭");
//        }
//        return voList;
//    }


//    @Override
//    public List<NewsVO> getNews(Integer current) {
//        List<NewsVO> newsList = new ArrayList<>();
//        WebClient webClient = WebClientUtils.getWebClient();
//        HtmlPage page;
//        try {
//            page = webClient.getPage(NEWS_BASE_URL + current);
//        } catch (IOException e) {
//            log.error("获取新闻信息URL失败");
//            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("获取新闻信息URL失败"));
//        }
//        try {
//            Document document = Jsoup.parse(page.asXml());
//            Elements articles = document.select("li.catlist_li");
//
//            for (Element article : articles) {
//                NewsVO news = new NewsVO();
//                // 获取日期
//                Element dateElement = article.select("span.f_r").first();
//                if (dateElement != null) {
//                    news.setDate(dateElement.text());
//                }
//                // 获取标题与链接
//                Element titleElement = article.select("a").first();
//                if (titleElement != null) {
//                    news.setTitle(titleElement.text().replaceAll(" ", ""));
//                    news.setUrl(titleElement.attr("href"));
//                }
//                newsList.add(news);
//            }
//        } catch (Exception e) {
//            log.error("获取新闻信息页面失败", e);
//            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("获取新闻信息页面失败"));
//        } finally {
//            // 确保释放资源
//            webClient.close();
//            log.info("WebClient 已关闭");
//        }
//        return newsList;
//    }

    @Autowired
    private CitrusPriceService citrusPriceService;
    @Autowired
    private CitrusNewsService citrusNewsService;

    @Override
    public PageResult<CitrusPrice> getPrice(GetPriceRequest request) {
        String category = request.getCategory();
        String region = request.getRegion();
        LambdaQueryWrapper<CitrusPrice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.isNotEmpty(category), CitrusPrice::getCategory, category)
                .like(StringUtils.isNotEmpty(region), CitrusPrice::getRegion, region);
        List<CitrusPrice> priceList = citrusPriceService.list(queryWrapper);
        if (CollUtil.isEmpty(priceList)){
            return null;
        }
        return getPage(priceList, request.getCurrent(), request.getSize());
    }

    @Override
    public PageResult<CitrusNews> getNews(Integer current, Integer pageSize) {
        List<CitrusNews> newsList = citrusNewsService.list();
        if (CollUtil.isEmpty(newsList)){
            return null;
        }
        return getPage(newsList, current, pageSize);
    }


}
