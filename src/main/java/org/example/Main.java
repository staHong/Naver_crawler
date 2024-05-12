package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);


        /*//Elasticsearch 서버 호스트 및 포트 설정
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.87.131", 9200, "http"))
        );

        try {
            // 검색 요청 생성
            SearchRequest searchRequest = new SearchRequest("naver_news");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 쿼리 설정 (여기서는 모든 문서를 검색)
            searchSourceBuilder.query(QueryBuilders.termQuery("date", "2023.11.02"));
            //searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.size(10000);  // 검색 결과의 크기를 설정 (기본값은 10)
            searchRequest.source(searchSourceBuilder);

            // 검색 응답 가져오기
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 결과 처리
            searchResponse.getHits().forEach(hit -> {
                // 각 문서의 필드 값 가져오기
                String title = hit.getSourceAsMap().get("title").toString();
                String content = hit.getSourceAsMap().get("content").toString();
                String date = hit.getSourceAsMap().get("date").toString();


                // 결과 출력
                System.out.println("Title: " + title);
                System.out.println("Content: " + content);
                System.out.println("date: " + date);
                System.out.println("--------------------");
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 클라이언트 종료
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        /*int minDate = 20231101;
        int maxDate = 20231130;
        int maxPage = 10;
        // Elasticsearch 서버의 호스트와 포트를 설정합니다.
        String hostname = "192.168.87.131";
        int port = 9200;

        try {
            NewsCrawler.crawlNews(minDate, maxDate, maxPage, hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
