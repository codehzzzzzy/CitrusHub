package com.hzzzzzy.job;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.entity.CitrusSupplyDemand;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.service.CitrusSupplyDemandService;
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
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.hzzzzzy.constant.CommonConstant.DEMAND;
import static com.hzzzzzy.constant.CommonConstant.SUPPLY;

/**
 * @author hzzzzzy
 * @date 2025/1/6
 * @description 定时爬取
 */
@Component
@Slf4j
public class CrawlSupplyAndDemandJob implements ApplicationRunner {

    @Autowired
    private CitrusSupplyDemandService citrusSupplyDemandService;

    List<CitrusSupplyDemand> supplyEntityList = new ArrayList<>();

    List<CitrusSupplyDemand> demandEntityList = new ArrayList<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    private final String baseUrl = "https://www.lvguo.net/ganju";

    private final String redirectBaseUrl = "https://www.lvguo.net";

    private final String supplyUrl = "/ganju/t1";

    private final String demandUrl = "/ganju/t2";

    @Override
    public void run(ApplicationArguments args) {
//        doCrawlOfSupply();
//        doCrawlOfDemand();
    }

    /**
     * 爬取供应信息（每天 12 点）
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void doCrawlOfSupply() {
        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page;

        try {
            // 获取页面
            page = webClient.getPage(baseUrl);
            HtmlAnchor anchor = page.getAnchorByHref(supplyUrl);
            // 点击链接，跳转到目标页面
            page = anchor.click();
        } catch (IOException e) {
            log.error("获取供应信息URL失败");
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("获取供应信息URL失败"));
        }

        try {
            // 解析页面内容
            Document document = Jsoup.parse(page.asXml());
            Elements elements = document.select("ul.list.cl li");

            if (elements.isEmpty()) {
                log.error("未找到供应信息列表");
                return;
            }
            // 遍历每个供应信息块
            for (int i = 0; i < elements.size(); i++) {
                // 解析
                Element current = elements.get(i);
                String releaseDate = current.select("div > p.cl > span").text();
                String timestamp = LocalDateTime.now().format(FORMATTER);
                if (!timestamp.equals(releaseDate)){
                    continue;
                }
                String title = current.select("h4.title > a").text();
                String region = current.select("a.litpic > span").text();
                String supply = current.select("div > p").first().text();
                String[] split = supply.split("：");
                String requireTime = split[0];
                String category = split[1];
                String replace = requireTime.replace("月", "");
                String[] requireTimeSplit = replace.split("-");
                if (requireTimeSplit[0].equals("常年")) {
                    requireTimeSplit = new String[]{"1", "12"};
                }
                String requireTimePre = requireTimeSplit[0];
                String requireTimeAfter = requireTimeSplit[1];
                // 提取图片链接
                String imageUrl = current.select("a.litpic > img").attr("src");
                // 提取详情链接
                String url = redirectBaseUrl + current.select("a.litpic").attr("href");
                // 构造对象并存储
                CitrusSupplyDemand entity = new CitrusSupplyDemand();
                entity.setCategory(category);
                entity.setRegion(region);
                entity.setReleaseDate(releaseDate);
                entity.setRequireTimePre(Integer.valueOf(requireTimePre));
                entity.setRequireTimeAfter(Integer.valueOf(requireTimeAfter));
                entity.setTitle(title);
                entity.setType(SUPPLY);
                entity.setImageUrl(imageUrl);
                entity.setUrl(url);
                log.info("解析到供应信息: {}", entity);
                supplyEntityList.add(entity);
            }
            citrusSupplyDemandService.saveBatch(supplyEntityList);
            log.info("供应信息爬取完成，解析到 {} 条数据", elements.size());
        } catch (Exception e) {
            log.error("解析供应信息页面失败", e);
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("解析供应信息页面失败"));
        } finally {
            // 确保释放资源
            webClient.close();
            supplyEntityList.clear();
            log.info("WebClient 已关闭");
        }
    }

    /**
     * 爬取求购信息（每天 12 点）
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void doCrawlOfDemand() {
        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page;

        try {
            // 获取页面
            page = webClient.getPage(baseUrl);
            HtmlAnchor anchor = page.getAnchorByHref(demandUrl);
            // 点击链接，跳转到目标页面
            page = anchor.click();
        } catch (IOException e) {
            log.error("获取求购信息URL失败");
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("获取求购信息URL失败"));
        }

        try {
            // 解析页面内容
            Document document = Jsoup.parse(page.asXml());
            Elements elements = document.select("ul.list.cl li");

            if (elements.isEmpty()) {
                log.error("未找到求购信息列表");
                return;
            }
            // 遍历每个供应信息块
            for (int i = 0; i < elements.size(); i++) {
                // 解析
                Element current = elements.get(i);
                String releaseDate = current.select("div > p.cl > span").text();
                String timestamp = LocalDateTime.now().format(FORMATTER);
                if (!timestamp.equals(releaseDate)){
                    continue;
                }
                String title = current.select("h4.title > a").text();
                String region = current.select("a.litpic > span").text();
                String supply = current.select("div > p").first().text();
                String[] split = supply.split("：");
                String requireTime = split[0];
                String category = split[1];
                String replace = requireTime.replace("月", "");
                String[] requireTimeSplit = replace.split("-");
                if (requireTimeSplit[0].equals("常年")) {
                    requireTimeSplit[0] = "1";
                    requireTimeSplit[1] = "12";
                }
                String requireTimePre = requireTimeSplit[0];
                String requireTimeAfter = requireTimeSplit[1];
                // 提取图片链接
                String imageUrl = current.select("a.litpic > img").attr("src");
                // 提取详情链接
                String url = redirectBaseUrl + current.select("a.litpic").attr("href");
                // 构造对象并存储
                CitrusSupplyDemand entity = new CitrusSupplyDemand();
                entity.setCategory(category);
                entity.setRegion(region);
                entity.setReleaseDate(releaseDate);
                entity.setRequireTimePre(Integer.valueOf(requireTimePre));
                entity.setRequireTimeAfter(Integer.valueOf(requireTimeAfter));
                entity.setTitle(title);
                entity.setType(DEMAND);
                entity.setImageUrl(imageUrl);
                entity.setUrl(url);
                log.info("解析到供应信息: {}", entity);
                demandEntityList.add(entity);
            }
            citrusSupplyDemandService.saveBatch(demandEntityList);
            log.info("求购信息爬取完成，解析到 {} 条数据", elements.size());
        } catch (Exception e) {
            log.error("解析求购信息页面失败", e);
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("解析求购信息页面失败"));
        } finally {
            // 确保释放资源
            webClient.close();
            demandEntityList.clear();
            log.info("WebClient 已关闭");
        }
    }
}
