import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.PriceVO;
import com.hzzzzzy.utils.WebClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/8
 * @description
 */
@Slf4j
public class crawl {

    private static final String BaseUrl = "https://www.cnhnb.com/hangqing/cdlist-2001686-0-0-0-0-1/";

    public static String replaceLastNumber(String input, String replacement) {
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

    public static void main(String[] args) {

        String region = "广东";
        String category = "";
//        String category = "耙耙柑";
        Integer current = 2;

        List<PriceVO> voList = new ArrayList<>();
        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page;
        try {
            // 获取页面
            page = webClient.getPage(BaseUrl);
            // region 不为空
            if (region != null && !region.isEmpty()) {
                HtmlAnchor anchor = findAnchorByRegion(page, region);
                if (anchor != null) {
                    // 跳转到目标页面
                    page = anchor.click();
                }
            }
            if (category != null && !category.isEmpty()) {
                HtmlAnchor anchor = findAnchorByRegion(page, category);
                if (anchor != null) {
                    // 跳转到目标页面
                    page = anchor.click();
                }
            }
            if (current != null) {
                String url = replaceLastNumber(page.getUrl().toString(), String.valueOf(current));
                page = webClient.getPage(url);
            }
        } catch (IOException e) {
            log.error("获取价格信息URL失败");
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("获取价格信息URL失败"));
        }

        try {
            Document document = Jsoup.parse(page.asXml());
            Elements listItems = document.select("li.market-list-item");

            for (Element listItem : listItems) {
                Element date = listItem.select("span.time").first();
                Element categoryElement = listItem.select("span.product").first();
                Element regionElement = listItem.select("span.place").first();
                Element price = listItem.select("span.price").first();
                Element lift = listItem.select("span.lifting.risecolor").first();
                PriceVO priceVO = new PriceVO();
                priceVO.setDate(date.text());
                priceVO.setCategory(categoryElement.text());
                priceVO.setRegion(regionElement.text());
                priceVO.setPrice(price.text());
                if (lift != null){
                    priceVO.setLift(lift.text());
                }
                voList.add(priceVO);
            }
        } catch (Exception e) {
            log.error("解析价格信息页面失败", e);
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("解析价格信息页面失败"));
        } finally {
            // 确保释放资源
            webClient.close();
            log.info("WebClient 已关闭");
            for (PriceVO vo : voList) {
                System.out.println(vo);
            }
        }
    }
}
