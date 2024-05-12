package org.example;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElasticsearchIndexer {
    public static void indexToElasticsearch(int id, String articleUrl, String title, String content,
                                            String writer, String date, String hostname, int port) {

        // Elasticsearch High Level REST Client를 초기화합니다.
        try (RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new org.apache.http.HttpHost(hostname, port, "http")))) {

            // 인덱스할 문서를 생성합니다.
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("id", id);
            jsonMap.put("articleUrl", articleUrl);
            jsonMap.put("title", title);
            jsonMap.put("content", content);
            jsonMap.put("writer", writer);
            jsonMap.put("date", date);

            // Elasticsearch에 인덱스를 요청합니다.
            IndexRequest request = new IndexRequest("naver_news").source(jsonMap, XContentType.JSON);
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            // 색인된 문서의 ID를 출력합니다.
            System.out.println("Indexed document ID: " + indexResponse.getId());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

