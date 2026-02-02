package com.example.rag.Rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;

import java.util.List;

public class WebSearchDocumentRetriever implements DocumentRetriever {

    private static final String TAVILY_API_KEY = "TAVILY_SEARCH_API_KEY";
    private static final String TAVILY_BASE_URL="https://api.tavily.com/search";

    @Override
    public List<Document> retrieve(Query query) {
        return List.of();
    }
}
