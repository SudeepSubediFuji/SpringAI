package com.example.rag.Rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WebSearchDocumentRetriever implements DocumentRetriever {

    Logger logger = Logger.getLogger(WebSearchDocumentRetriever.class.getName());

    private static final String TAVILY_API_KEY = "TAVILY_API_KEY";
    private static final String TAVILY_BASE_URL = "https://api.tavily.com/search";
    private static final int DEFAULT_RESULT_LIMIT = 5;
    private final int resultLimit;
    private final RestClient restClient;

    public WebSearchDocumentRetriever(RestClient.Builder restClientBuilder, int resultLimit) {
        Assert.notNull(restClientBuilder, "Client builder cannot be null");
        String API_KEY = System.getenv(TAVILY_API_KEY);
        Assert.hasText(API_KEY, "Environment variable " + TAVILY_API_KEY + " must be set.");
        this.restClient = restClientBuilder
                .baseUrl(TAVILY_BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
                .build();
        if (resultLimit <= 0) {
            throw new IllegalArgumentException("resultLimit must be greater than o");
        }
        this.resultLimit = resultLimit;
    }

    @Override
    public List<Document> retrieve(Query query) {
        logger.info("Processing Query: {}" + query.text());
        Assert.notNull(query, "Query cannot be null.");

        String q = query.text();
        Assert.hasText(q, "Query.text() cannot be null.");
        //.body(TavilyResponsePayload.class) --> here this method will typeCast the responsePayload
        TavilyResponsePayload responsePayload = restClient.post()
                .body(new TavilyRequestPayload(q, "advanced", resultLimit))
                .retrieve()
                .body(TavilyResponsePayload.class);
//        logger.info("ResponseResult : " + responsePayload.results() + "ResponseOnly: " + responsePayload + responsePayload.results());

        if (responsePayload == null || CollectionUtils.isEmpty(responsePayload.results())) {
            return List.of();
        }
        List<Document> documents = new ArrayList<>(responsePayload.results.size());

        for (TavilyResponsePayload.Hit hit : responsePayload.results()) {
            // Map each Tavily hit into a Spring AI Document with metadata and score.
            Document doc = Document.builder()
                    .text(hit.content())
                    .score(hit.score())
                    .metadata("title", hit.title)
                    .metadata("url", hit.url)
                    .build();
            documents.add(doc);
        }
        return documents;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record TavilyRequestPayload(String query, String searchDepth, int maxResult) {
    }

    record TavilyResponsePayload(List<Hit> results) {
        record Hit(String title, String url, String content, Double score) {
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private RestClient.Builder restClientBuilder;
        private int resultLimit = DEFAULT_RESULT_LIMIT;

        private Builder() {
        }


        public Builder restClientBuilder(RestClient.Builder restClientBuilder) {
            this.restClientBuilder = restClientBuilder;
            return this;
        }

        public Builder maxResults(int maxResults) {
            if (maxResults <= 0) {
                throw new IllegalArgumentException("maxResults is not set. ");
            }
            this.resultLimit = maxResults;
            return this;
        }

        public WebSearchDocumentRetriever build() {
            return new WebSearchDocumentRetriever(restClientBuilder, resultLimit);
        }
    }

}
