package com.example.SpringAIVector.controller;

//import com.example.SpringAIVector.advisor.tokenUsageAdvisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping
public class CompanyInfoPdfController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    Logger logger = Logger.getLogger(CompanyInfoPdfController.class.getName());

    @Value("classpath:/promptTemplates/CompanyInfoTemplate.st")
    Resource CompanyInfoTemplate;

    public CompanyInfoPdfController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/companyInfo")
    public ResponseEntity<String> fujiBot(@RequestHeader("username") String username,
                                          @RequestParam("message") String message) {
        logger.info("RAG request received from user: " + username + " with message: " + message);
        // SearchRequestでは、どういう行動で検索するのかを設定します。
//        SearchRequest similarDoc =
//                SearchRequest.builder().query(message).topK(3).similarityThreshold(0.7).build();
//        //　VectorStoreにそのSearchRequestインスタンスをDocumentのList型のオブジェクトに設定します。
//        List<Document> similaritySearch = vectorStore.similaritySearch(similarDoc);
//        //　そのDocumentのList型のオブジェクトからString型に変換するため、Streamインスタンスでmap機能を使って、テクストに変換し、Collectorを使ってラインセパレート・ラインを消します。
//       String similarText = similaritySearch.stream()
//               .map(Document::getText)
//               .collect(Collectors.joining(System.lineSeparator()));
        String answer = chatClient.prompt(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
//               .system(promptSystemSpec -> promptSystemSpec.text(CompanyInfoTemplate).param("companyInfo",similarText))
                .user(message).call().content();
        logger.info("RAG answer generated for user: " + answer);
        return ResponseEntity.ok(answer);
    }
}
