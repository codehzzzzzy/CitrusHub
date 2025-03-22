package com.hzzzzzy.job;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.entity.CitrusNews;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.NewsVO;
import com.hzzzzzy.service.CitrusNewsService;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class CrawlNewsJob implements ApplicationRunner {

    @Autowired
    private CitrusNewsService citrusNewsService;

    List<CitrusNews> newsList = new ArrayList<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat SIMPLE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final String NEWS_BASE_URL = "https://news.foodmate.net/search.php?moduleid=21&spread=0&kw=%E6%9F%91%E6%A9%98&page=";

    @Override
    public void run(ApplicationArguments args) {
//        doCrawl();
    }

    /**
     * 爬取新闻信息（每天 12 点）
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void doCrawl() {
        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page;
        try {
            page = webClient.getPage(NEWS_BASE_URL + 1);
        } catch (IOException e) {
            log.error("获取新闻信息URL失败");
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("获取新闻信息URL失败"));
        }
        try {
            Document document = Jsoup.parse(page.asXml());
            Elements articles = document.select("li.catlist_li");
            LocalDate currentDate = LocalDate.now();

            for (Element article : articles) {
                CitrusNews news = new CitrusNews();
                // 获取日期
                Element dateElement = article.select("span.f_r").first();
                if (dateElement != null) {
                    String dateStr = dateElement.text();
                    LocalDate newsDate = LocalDateTime.parse(dateStr, FORMATTER).toLocalDate();
                    if (currentDate.isBefore(newsDate)) {
                        continue; // 如果当前日期小于新闻日期，跳过该条新闻
                    }
                    news.setDate(SIMPLE_FORMATTER.parse(dateStr));
                }
                // 获取标题与链接
                Element titleElement = article.select("a").first();
                if (titleElement != null) {
                    news.setTitle(titleElement.text().replaceAll(" ", ""));
                    news.setUrl(titleElement.attr("href"));
                }
                newsList.add(news);
            }
            citrusNewsService.saveBatch(newsList);
        } catch (Exception e) {
            log.error("获取新闻信息页面失败", e);
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("获取新闻信息页面失败"));
        } finally {
            // 确保释放资源
            webClient.close();
            newsList.clear();
            log.info("WebClient 已关闭");
        }
    }
}
