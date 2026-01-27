package com.example.SpringAIVector.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ragController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ragController(@Qualifier("OpenAiChatClient") ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient=chatClient;
        this.vectorStore=vectorStore;
    }

    @Value("classpath:/PromptTemplates/ZooSystemsTemplate.st")
    Resource PromptTemplate;

    @GetMapping("/rag" )
    public String some(){
        return "hw";
    }
}
