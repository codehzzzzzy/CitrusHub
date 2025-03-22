package com.hzzzzzy.job;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.entity.CitrusPrice;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.service.CitrusPriceService;
import com.hzzzzzy.utils.WebClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description 定时爬取
 */
@Component
@Slf4j
public class CrawlPriceJob implements ApplicationRunner {

    @Autowired
    private CitrusPriceService citrusPriceService;

    List<CitrusPrice> priceList = new ArrayList<>();

    private static final SimpleDateFormat SIMPLE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String PRICE_BASE_URL = "https://www.cnhnb.com/hangqing/cdlist-2001686-0-0-0-0-1/";

    @Override
    public void run(ApplicationArguments args) {
//        doCrawl1();
//        doCrawl2();
    }

    /**
     * 爬取价格信息
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void doCrawl2() {
        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page;
        Integer current = 3;
        try {
            while (true) {
                page = webClient.getPage(PRICE_BASE_URL);
                String url = replaceLastNumber(page.getUrl().toString(), String.valueOf(current));
                page = webClient.getPage(url);
                Document document = Jsoup.parse(page.asXml());
                Elements listItems = document.select("li.market-list-item");
                if (listItems.isEmpty()) {
                    return;
                }
                for (Element listItem : listItems) {
                    Element date = listItem.select("span.time").first();
                    String timestamp = LocalDateTime.now().format(FORMATTER);
                    if (!timestamp.equals(date)){
                        continue;
                    }
                    Element categoryElement = listItem.select("span.product").first();
                    Element regionElement = listItem.select("span.place").first();
                    Element price = listItem.select("span.price").first();
                    Element lift = listItem.select("span.lifting.risecolor").first();
                    CitrusPrice citrusPrice = new CitrusPrice();
                    citrusPrice.setDate(SIMPLE_FORMATTER.parse(date.text()));
                    citrusPrice.setCategory(categoryElement.text());
                    citrusPrice.setRegion(regionElement.text());
                    citrusPrice.setPrice(price.text());
                    if (lift != null) {
                        citrusPrice.setLift(lift.text());
                    }
                    priceList.add(citrusPrice);
                }
                current++;
                if (current > 4) {
                    return;
                }
            }
        } catch (Exception e) {
            log.error("解析价格信息页面失败", e);
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("解析价格信息页面失败"));
        } finally {
            // 确保释放资源
            citrusPriceService.saveBatch(priceList);
            webClient.close();
            priceList.clear();
            log.info("WebClient 已关闭");
        }
    }

    /**
     * 爬取价格信息
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void doCrawl1() {
        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page;
        Integer current = 1;
        try {
            while (true) {
                page = webClient.getPage(PRICE_BASE_URL);
                String url = replaceLastNumber(page.getUrl().toString(), String.valueOf(current));
                page = webClient.getPage(url);
                Document document = Jsoup.parse(page.asXml());
                Elements listItems = document.select("li.market-list-item");
                if (listItems.isEmpty()) {
                    return;
                }
                for (Element listItem : listItems) {
                    Element date = listItem.select("span.time").first();
                    String timestamp = LocalDateTime.now().format(FORMATTER);
                    if (!timestamp.equals(date)){
                        continue;
                    }
                    Element categoryElement = listItem.select("span.product").first();
                    Element regionElement = listItem.select("span.place").first();
                    Element price = listItem.select("span.price").first();
                    Element lift = listItem.select("span.lifting.risecolor").first();
                    CitrusPrice citrusPrice = new CitrusPrice();
                    citrusPrice.setDate(SIMPLE_FORMATTER.parse(date.text()));
                    citrusPrice.setCategory(categoryElement.text());
                    citrusPrice.setRegion(regionElement.text());
                    citrusPrice.setPrice(price.text());
                    if (lift != null) {
                        citrusPrice.setLift(lift.text());
                    }
                    priceList.add(citrusPrice);
                }
                current++;
                if (current > 2) {
                    return;
                }
            }
        } catch (Exception e) {
            log.error("解析价格信息页面失败", e);
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("解析价格信息页面失败"));
        } finally {
            // 确保释放资源
            citrusPriceService.saveBatch(priceList);
            webClient.close();
            priceList.clear();
            log.info("WebClient 已关闭");
        }
    }

    private static String replaceLastNumber(String input, String replacement) {
        return input.replaceAll("\\d+(?!.*\\d)", replacement);
    }

    private static HtmlAnchor findAnchorByRegion(HtmlPage page, String region) {
        for (HtmlAnchor anchor : page.getAnchors()) {
            if (region.equals(anchor.asText().trim())) {
                return anchor;
            }
        }
        return null;
    }
}
