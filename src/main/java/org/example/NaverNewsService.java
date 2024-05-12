package org.example;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NaverNewsService {
    // Main 쿼리
    public List<NaverNewsDto> searchByQuery(String selectedOption, String searchWord){

        //Elasticsearch 서버 호스트 및 포트 설정
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.87.131", 9200, "http"))
        );

        List<NaverNewsDto> naverNewsDtoList = new ArrayList<>();
        try {
            // 검색 요청 생성
            SearchRequest searchRequest = new SearchRequest("naver_news");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            if(searchWord.equals("")){
                // 쿼리 설정 (여기서는 모든 문서를 검색)
                searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            } else{ // 쿼리 설정 (여기서는 검색옵션과 워드에 맞게)
                // 제목 선택 시
                if (selectedOption.equals("title")){
                    searchSourceBuilder.query(QueryBuilders.termQuery("title", searchWord));
                }else if(selectedOption.equals("content")){ //내용 선택시
                    searchSourceBuilder.query(QueryBuilders.termQuery("content", searchWord));
                } else{ // 작성자 선택 시
                    searchSourceBuilder.query(QueryBuilders.termQuery("writer", searchWord));
                }
            }

            searchSourceBuilder.size(10000);  // 검색 결과의 크기를 설정 (기본값은 10)
            searchRequest.source(searchSourceBuilder);

            // 검색 응답 가져오기
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 결과 처리
            searchResponse.getHits().forEach(hit -> {
                // 각 문서의 필드 값 가져오기
                int id = (int)hit.getSourceAsMap().get("id");
                String title = hit.getSourceAsMap().get("title").toString();
                String content = hit.getSourceAsMap().get("content").toString();
                String date = hit.getSourceAsMap().get("date").toString();
                String articleUrl = hit.getSourceAsMap().get("articleUrl").toString();
                String writer = hit.getSourceAsMap().get("writer").toString();
                NaverNewsDto naverNewsDto = new NaverNewsDto();
                naverNewsDto.setId(id);
                naverNewsDto.setTitle(title);
                naverNewsDto.setContent(content);
                naverNewsDto.setArticleUrl(articleUrl);
                naverNewsDto.setDate(date);
                naverNewsDto.setWriter(writer.substring(0,3));

                naverNewsDtoList.add(naverNewsDto);

                // 결과 출력
                // System.out.println("Title: " + title);
                // System.out.println("Content: " + content);
                // System.out.println("date: " + date);
                // System.out.println("--------------------");
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
        }
        return naverNewsDtoList;
    }

    // Detail 쿼리
    public NaverNewsDto searchById(int id) {
        //Elasticsearch 서버 호스트 및 포트 설정
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.87.131", 9200, "http"))
        );
        NaverNewsDto naverNewsDto = new NaverNewsDto();

        try {

            // 검색 요청 생성
            SearchRequest searchRequest = new SearchRequest("naver_news");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // 쿼리 설정 (여기서는 id로 기사 뽑기)
            searchSourceBuilder.query(QueryBuilders.termQuery("id", id));
            searchSourceBuilder.size(10000);  // 검색 결과의 크기를 설정 (기본값은 10)
            searchRequest.source(searchSourceBuilder);

            // 검색 응답 가져오기
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 결과 처리
            SearchHits hits = searchResponse.getHits();
            SearchHit hit = hits.getAt(0); // 첫 번째 결과만 가져옴

            String title = hit.getSourceAsMap().get("title").toString();
            String content = hit.getSourceAsMap().get("content").toString();
            String date = hit.getSourceAsMap().get("date").toString();
            String articleUrl = hit.getSourceAsMap().get("articleUrl").toString();
            String writer = hit.getSourceAsMap().get("writer").toString();

            naverNewsDto.setId(id);
            naverNewsDto.setTitle(title);
            naverNewsDto.setContent(content);
            naverNewsDto.setArticleUrl(articleUrl);
            naverNewsDto.setDate(date);
            naverNewsDto.setWriter(writer.substring(0,3));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 클라이언트 종료
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return naverNewsDto;
    }
}
