package com.example.rag.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatClient webSearchRagClient;

    public RagController(@Qualifier("ChatMemory") ChatClient chatClient,
                         @Qualifier("webSearchRagClient") ChatClient webSearchRagClient,
                         VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.webSearchRagClient = webSearchRagClient;
    }

    @GetMapping("/chating")
    public String chats(@RequestParam("message") String message){
        return chatClient.prompt().user(message).call().content();
    }

    @GetMapping("/rag")
    public ResponseEntity<String> ragOperation(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .call().content();
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/webSearch")
    public ResponseEntity<String> webSearchOps(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {

        String answer = webSearchRagClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);
    }


}
