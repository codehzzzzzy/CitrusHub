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
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
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
public class CrawlJob implements ApplicationRunner {

    @Autowired
    private CitrusSupplyDemandService citrusSupplyDemandService;

    List<CitrusSupplyDemand> supplyEntityList = new ArrayList<>();

    List<CitrusSupplyDemand> demandEntityList = new ArrayList<>();

    private final String BaseUrl = "https://www.lvguo.net/ganju";

    private final String SupplyUrl = "/ganju/t1";

    private final String DemandUrl = "/ganju/t2";

    @Override
    public void run(ApplicationArguments args) {
//        doCrawlOfSupply();
//        doCrawlOfDemand();
    }

    /**
     * 爬取供应信息（每天 6 点）
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void doCrawlOfSupply() {
        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page;

        try {
            // 获取页面
            page = webClient.getPage(BaseUrl);
            HtmlAnchor anchor = page.getAnchorByHref(SupplyUrl);
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
            elements.forEach(element -> {
                // 解析
                String title = element.select("h4.title > a").text();
                String region = element.select("a.litpic > span").text();
                String supply = element.select("div > p").first().text();
                String[] split = supply.split("：");
                String requireTime = split[0];
                String category = split[1];
                String releaseDate = element.select("div > p.cl > span").text();
                String replace = requireTime.replace("月", "");
                String[] requireTimeSplit = replace.split("-");
                if (requireTimeSplit[0].equals("常年")) {
                    requireTimeSplit = new String[]{"1", "12"};
                }
                String requireTimePre = requireTimeSplit[0];
                String requireTimeAfter = requireTimeSplit[1];
                // 构造对象并存储
                CitrusSupplyDemand entity = new CitrusSupplyDemand();
                entity.setCategory(category);
                entity.setRegion(region);
                entity.setReleaseDate(releaseDate);
                entity.setRequireTimePre(Integer.valueOf(requireTimePre));
                entity.setRequireTimeAfter(Integer.valueOf(requireTimeAfter));
                entity.setTitle(title);
                entity.setType(SUPPLY);
                log.info("解析到供应信息: {}", entity);
                supplyEntityList.add(entity);
                // TODO 优化
                citrusSupplyDemandService.saveBatch(supplyEntityList);
            });

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
     * 爬取求购信息（每天 6 点）
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void doCrawlOfDemand() {
        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page;

        try {
            // 获取页面
            page = webClient.getPage(BaseUrl);
            HtmlAnchor anchor = page.getAnchorByHref(DemandUrl);
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
            elements.forEach(element -> {
                // 解析
                String title = element.select("h4.title > a").text();
                String region = element.select("a.litpic > span").text();
                String supply = element.select("div > p").first().text();
                String[] split = supply.split("：");
                String requireTime = split[0];
                String category = split[1];
                String releaseDate = element.select("div > p.cl > span").text();
                String replace = requireTime.replace("月", "");
                String[] requireTimeSplit = replace.split("-");
                if (requireTimeSplit[0].equals("常年")) {
                    requireTimeSplit[0] = "1";
                    requireTimeSplit[1] = "12";
                }
                String requireTimePre = requireTimeSplit[0];
                String requireTimeAfter = requireTimeSplit[1];
                // 构造对象并存储
                CitrusSupplyDemand entity = new CitrusSupplyDemand();
                entity.setCategory(category);
                entity.setRegion(region);
                entity.setReleaseDate(releaseDate);
                entity.setRequireTimePre(Integer.valueOf(requireTimePre));
                entity.setRequireTimeAfter(Integer.valueOf(requireTimeAfter));
                entity.setTitle(title);
                entity.setType(DEMAND);
                log.info("解析到供应信息: {}", entity);
                demandEntityList.add(entity);
                // TODO 优化
                citrusSupplyDemandService.saveBatch(demandEntityList);
            });

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
