import com.hzzzzzy.model.vo.NewsVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsScraper {

    public static void main(String[] args) {
        List<NewsVO> newsList = new ArrayList<>();
        int page = 1;
        try {
            Document doc = Jsoup.connect("https://news.foodmate.net/search.php?moduleid=21&spread=0&kw=%E6%9F%91%E6%A9%98&page=" + page).get();
            Elements articles = doc.select("li.catlist_li");

            for (Element article : articles) {
                NewsVO news = new NewsVO();
                // 获取日期
                Element dateElement = article.select("span.f_r").first();
                if (dateElement != null) {
                    news.setDate(dateElement.text());
                }

                // 获取标题
                Element titleElement = article.select("a").first();
                if (titleElement != null) {
                    news.setTitle(titleElement.text());
                    news.setUrl(titleElement.attr("href"));
                }

                newsList.add(news);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (newsList.isEmpty()){
            System.out.println("获取新闻列表失败");
            return;
        }
        for (NewsVO news : newsList) {
            System.out.println("日期: " + news.getDate());
            System.out.println("标题: " + news.getTitle());
            System.out.println("链接: " + news.getUrl());
            System.out.println("--------------------------------------------------");
        }
    }
}
