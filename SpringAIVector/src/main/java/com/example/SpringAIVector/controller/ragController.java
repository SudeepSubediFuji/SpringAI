package com.example.SpringAIVector.controller;

import org.apache.logging.log4j.simple.SimpleLogger;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping
public class ragController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    Logger logger = Logger.getLogger(ragController.class.getName());

    @Value("classpath:/promptTemplates/ZooSystemTemplate.st")
    Resource promptTemplate;

    public ragController(ChatClient.Builder builder,VectorStore vectorStore) {
        this.chatClient=builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
        this.vectorStore=vectorStore;
    }

    @GetMapping("/animalInfo" )
    public ResponseEntity<String> zooBot(@RequestHeader("username") String username,
                                         @RequestParam("message") String message) {
        logger.info("RAG request received from user: " + username + " with message: " + message);
        SearchRequest similarDoc =
                SearchRequest.builder().query(message).topK(3).similarityThreshold(0.3).build();
        List<Document> similaritySearch = vectorStore.similaritySearch(similarDoc);
       String similarText = similaritySearch.stream()
               .map(Document::getText)
               .collect(Collectors.joining(System.lineSeparator()));
       String answer = chatClient.prompt(message)
               .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID,username))
               .system(promptSystemSpec -> promptSystemSpec.text(promptTemplate).param("documents",similarText))
               .user(message).call().content();
       logger.info("RAG answer generated for user: " + answer);
       return ResponseEntity.ok(answer);
    }
    @GetMapping("/rag2")
    public String zoo(String message){
        return chatClient.prompt(message).user(promptTemplate).call().content();
    }

    @Value("classpath:/promptTemplates/XarvisSystemTemplate.st")
    Resource XarvisSystemTemplate;

    @GetMapping("/xarvis")
    public ResponseEntity<String> xarvisController(@RequestHeader("username") String username,
                                                      @RequestParam("message") String message){
        SearchRequest similarDoc =
                SearchRequest.builder().query(message).topK(3).similarityThreshold(0.7).build();
        List<Document> similarSearch = vectorStore.similaritySearch(similarDoc);
        String similarPattern = similarSearch.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        String answer = chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID,username))
                .system(promptSystemSpec -> promptSystemSpec.text(XarvisSystemTemplate).param("personalDocuments",similarPattern))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);
    }

}
