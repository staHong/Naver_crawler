package org.example;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsCrawler {
    static int id = 0;
    public static void crawlNews(int minDate, int maxDate, int maxPage, String hostname, int port) throws IOException {

        for (int i = minDate; i <= maxDate; i++) {
            for (int j = 1; j <= maxPage; j++) {
                String url = "https://news.naver.com/main/list.naver?mode=LSD&mid=shm&sid1=100&date=" + i + "&page=" + j;

                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.getElementsByAttributeValue("class", "list_body newsflash_body");

                Element element = elements.get(0);

                Elements photoElements = element.getElementsByAttributeValue("class", "photo");

                processNewsElements(photoElements, hostname, port);
                System.out.println(j + "page 크롤링 종료");
            }
            System.out.println(i + "날짜 크롤링 종료");
        }
    }

    private static void processNewsElements(Elements photoElements, String hostname, int port) throws IOException {
        for (int z = 0; z < photoElements.size(); z++) {
            Element articleElement = photoElements.get(z);
            Elements aElements = articleElement.select("a");
            if (!aElements.isEmpty()) {
                Element aElement = aElements.get(0);
                String articleUrl = aElement.attr("href"); // 기사링크
                Element imgElement = aElement.select("img").get(0);
                String title = imgElement.attr("alt"); // 기사제목

                try {
                    Document subDoc = Jsoup.connect(articleUrl).get();
                    Element contentElement = subDoc.getElementById("dic_area");

                    Element dateElement = subDoc.getElementsByAttributeValue("class", "media_end_head_info_datestamp_time _ARTICLE_DATE_TIME").first();
                    Element writerElement = subDoc.getElementsByAttributeValue("class", "byline_s").first();

                    // 기자이름이 안적혀져 있는 경우 해당 기사 저장 X, 내용이 없는 경우, 날짜가 없는 경우
                    if (writerElement != null && contentElement != null && dateElement != null) {
                        String writer = writerElement.text(); // 기자이름
                        String content = contentElement.text(); // 내용
                        String date = dateElement.text(); // 날짜
                        id++;
                        ElasticsearchIndexer.indexToElasticsearch(id, articleUrl, title, content, writer, date, hostname, port);
                    }
                } catch (HttpStatusException e) {
                    System.err.println("HTTP Status Exception for URL: " + articleUrl);
                    System.err.println("Status code: " + e.getStatusCode());
                }
            }
        }
    }
}
