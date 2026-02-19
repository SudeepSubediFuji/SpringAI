package com.example.Observability.controller;

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

    public RagController(@Qualifier("ChatMemory") ChatClient chatClient,
                         VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
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


}
